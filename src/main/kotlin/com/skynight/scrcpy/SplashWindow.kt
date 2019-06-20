package com.skynight.scrcpy

import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFilesMD5
import com.skynight.scrcpy.base.exitButton
import org.apache.commons.codec.digest.DigestUtils
import java.awt.Color
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.io.File
import java.io.FileInputStream
import javax.swing.JPanel
import javax.swing.*
import kotlin.math.log

class SplashWindow : JFrame() {

    init {
        val jsonObject = ControlCenter.getInstance().getLoadLanguage().getWindowStrings("SplashWindow")
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(300, 120)
        setLocation((screenSize.width - 300) / 2, (screenSize.height - 120) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = true
        title = jsonObject.get("title").asString
        addComponentListener(object : ComponentListener {
            override fun componentMoved(e: ComponentEvent?) {
                setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
            }

            override fun componentResized(e: ComponentEvent?) {

            }

            override fun componentHidden(e: ComponentEvent?) {
            }

            override fun componentShown(e: ComponentEvent?) {

            }

        })

        val panel = JPanel()
        add(panel)
        panel.background = Color.WHITE
        panel.layout = null
        panel.isVisible = false
        panel.setSize(300, 100)

        val content = JLabel(jsonObject.get("check_file").asString, JLabel.CENTER)
        content.setBounds(0, 0, width - 16, 15)
        panel.add(content)
        panel.isVisible = true

        Thread {

            if (!checkFiles()) {
                content.text = jsonObject.get("check_file_fail").asString
                exitButton(this, panel)
                return@Thread
            }

            content.text =jsonObject.get("check_md5").asString

            if (!checkMD5Sum()) {
                content.text = jsonObject.get("check_md5_fail").asString/*jsonObject.get("check_file_fail").asString*/
                exitButton(this, panel)
                return@Thread
            }

            ControlCenter.getInstance().getControlListener().passFileCheck()
            dispose()
            return@Thread
        }.start()
    }

    private fun checkFiles(): Boolean {
        val dir = System.getProperty("user.dir") + File.separator
        for (i in PackageFileList) {
            val file = File(dir + i)
            LogOutputWindow.takeLog("CheckFile$file")
            if (!file.exists()) {
                LogOutputWindow.takeLog("CheckFile Error at $i")
                return false
            }
        }
        return true
    }

    private fun checkMD5Sum(): Boolean {
        val dir = System.getProperty("user.dir") + File.separator
        for (i in PackageFileList) {
            val file = File(dir + i)
            LogOutputWindow.takeLog("CheckMD5 ${file.path}: ${PackageFilesMD5[i]}")
            if (PackageFilesMD5[i] != DigestUtils.md5Hex(FileInputStream(file))) {
                LogOutputWindow.takeLog("CheckMD5 Error at $i")
                return false
            }
        }
        return true
    }

}
