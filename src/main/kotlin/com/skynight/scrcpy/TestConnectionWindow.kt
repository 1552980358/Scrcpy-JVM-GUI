package com.skynight.scrcpy

import com.skynight.scrcpy.base.*
import com.skynight.scrcpy.widgets.Label
import com.skynight.scrcpy.widgets.Panel
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JProgressBar

class TestConnectionWindow : JFrame() {

    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val jsonObject = LoadLanguage.instance.getWindowStrings("TestConnectionWindow")

        title = jsonObject.get("title").asString
        setSize(300, 120)
        isAlwaysOnTop = true
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
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

        val panel = Panel(width, height, null)
        add(panel)
        panel.isVisible = false

        val content = Label(jsonObject.get("connection_check").asString, JLabel.CENTER, 0, 0, width - 16, 15)
        LogOutputWindow.takeLog("Connection Check Start")
        panel.add(content)
        panel.isVisible = true

        Thread {

            if (!checkAdbConnect()) {
                content.text = jsonObject.get("connection_check_fail").asString
                LogOutputWindow.takeLog("Connection Check Fail")
                exitButton(this, panel)
                return@Thread
            }

            val jProgressBar = JProgressBar()
            panel.add(jProgressBar)
            jProgressBar.foreground = ControlCenter.instance.getBGColor()
            jProgressBar.setBounds(10, 15, 260, 40)
            jProgressBar.maximum = 3
            jProgressBar.value = 0
            jProgressBar.isVisible = true

            title = jsonObject.get("connection_check_succeed").asString
            LogOutputWindow.takeLog("Connection Check Pass")

            ControlCenter.instance.getControlListener().passAdbCheck()

            for (i: Int in 1..3) {
                content.text = String.format(jsonObject.get("time_remain").asString, 4 - i)
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    //
                }
                LogOutputWindow.takeLog("${4-i}seconds left")
                jProgressBar.value = i
            }
            dispose()
        }.start()
    }
}