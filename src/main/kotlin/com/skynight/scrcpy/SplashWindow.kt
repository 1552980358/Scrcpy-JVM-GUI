package com.skynight.scrcpy

import com.skynight.scrcpy.Base.ControlCenter
import com.skynight.scrcpy.Base.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.Base.exitButton
import java.awt.Color
import java.awt.Toolkit
import java.io.File
import javax.swing.JPanel
import javax.swing.*

class SplashWindow : JFrame("启动中, 请稍后...") {

    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(300, 120)
        setLocation((screenSize.width - 300) / 2, (screenSize.height - 120) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = true
        val panel = JPanel()
        add(panel)
        panel.background = Color.WHITE
        panel.layout = null
        panel.isVisible = false
        panel.setSize(300, 100)

        val content = JLabel("检查文件包...", JLabel.CENTER)
        content.setBounds(0, 0, 300, 15)
        panel.add(content)
        panel.isVisible = true

        Thread {

            if (!checkFiles()) {
                content.text = "文件包检查失败!详细请查看弹窗!"
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
                //println("此文件无法找到, 请检查是否存在：\n$file")
                return false
            }
        }
        return true
    }

}
