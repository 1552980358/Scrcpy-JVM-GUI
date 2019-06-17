package com.skynight.scrcpy

import com.skynight.scrcpy.Base.ControlCenter
import com.skynight.scrcpy.Base.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.Base.DecodeLanguagePack
import com.skynight.scrcpy.Base.exitButton
import java.awt.Color
import java.awt.Toolkit
import java.io.File
import javax.swing.JPanel
import javax.swing.*

class SplashWindow : JFrame() {

    init {
        val jsonObject = DecodeLanguagePack.getInstance().getWindowStrings("SplashWindow")
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(300, 120)
        setLocation((screenSize.width - 300) / 2, (screenSize.height - 120) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = true
        title = jsonObject.get("title").asString
        val panel = JPanel()
        add(panel)
        panel.background = Color.WHITE
        panel.layout = null
        panel.isVisible = false
        panel.setSize(300, 100)

        val content = JLabel(jsonObject.get("check_file").asString, JLabel.CENTER)
        content.setBounds(0, 0, 300, 15)
        panel.add(content)
        panel.isVisible = true

        Thread {

            if (!checkFiles()) {
                content.text = jsonObject.get("check_file_fail").asString
                exitButton(this, panel)
                return@Thread
            }

            ControlCenter.getInstance().controlListener.passFileCheck()
            dispose()
            return@Thread
        }.start()
    }

    private fun checkFiles(): Boolean {
        val dir = System.getProperty("user.dir") + File.separator
        for (i in PackageFileList) {
            val file = File(dir + i)
            println(file.toString())
            if (!file.exists()) {
                return false
            }
        }
        return true
    }

}
