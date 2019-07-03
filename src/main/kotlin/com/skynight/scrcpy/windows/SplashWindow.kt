package com.skynight.scrcpy.windows

import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFilesMD5
import com.skynight.scrcpy.base.LoadLanguage
import com.skynight.scrcpy.base.exitButton
import com.skynight.scrcpy.widgets.Label
import com.skynight.scrcpy.widgets.Label.Companion.LABEL_CENTER
import com.skynight.scrcpy.widgets.Panel
import org.apache.commons.codec.digest.DigestUtils
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.io.File
import java.io.FileInputStream
import javax.swing.JFrame
import javax.swing.UIManager


class SplashWindow : JFrame() {

    init {
        val jsonObject = LoadLanguage.getLoadLanguage.getWindowStrings("SplashWindow")
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

        val panel = Panel(width, height - 20, null)
        add(panel)
        panel.isVisible = false

        val content = Label(jsonObject.get("check_file").asString, LABEL_CENTER, 0, 0, width - 16, 15)
        panel.add(content)
        panel.isVisible = true

        Thread {

            LogOutputWindow.takeLog("File Check Start")

            if (!checkFiles()) {
                content.text = jsonObject.get("check_file_fail").asString
                exitButton(this, panel)
                return@Thread
            }

            content.text =jsonObject.get("check_md5").asString

            if (!checkMD5Sum()) {
                content.text = jsonObject.get("check_md5_fail").asString
                exitButton(this, panel)
                return@Thread
            }

            LogOutputWindow.takeLog("Splash Check Pass").newLine()
            ControlCenter.getControlCenter.getControlListener().passFileCheck()
            dispose()
            return@Thread
        }.start()
    }

    private fun checkFiles(): Boolean {
        val dir = System.getProperty("user.dir") + File.separator
        for (i in PackageFileList) {
            val file = File(dir + i)
            LogOutputWindow.takeLog("File Check$file")
            if (!file.exists()) {
                LogOutputWindow.takeLog("File Check Error at $i").newLine()
                return false
            }
        }
        LogOutputWindow.takeLog("File Check Pass").newLine()
        return true
    }

    private fun checkMD5Sum(): Boolean {
        val dir = System.getProperty("user.dir") + File.separator
        for (i in PackageFileList) {
            val file = File(dir + i)
            LogOutputWindow.takeLog("MD5 Check ${file.path}: ${PackageFilesMD5[i]}")
            if (PackageFilesMD5[i] != DigestUtils.md5Hex(FileInputStream(file))) {
                LogOutputWindow.takeLog("MD5 Check Error at $i").newLine()
                return false
            }
        }
        LogOutputWindow.takeLog("MD5 Check Pass").newLine()
        return true
    }

}
