package com.skynight.scrcpy

import com.skynight.scrcpy.base.*
import java.awt.Color
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar

class TestConnectionWindow : JFrame() {

    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val jsonObject = ControlCenter.getInstance().getLoadLanguage().getWindowStrings("TestConnectionWindow")

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

        val panel = JPanel()
        add(panel)
        panel.background = Color.WHITE
        panel.layout = null
        panel.isVisible = false
        panel.setSize(width, height)

        val content = JLabel(jsonObject.get("connection_check").asString, JLabel.CENTER)
        content.setBounds(0, 0, width - 16, 15)
        panel.add(content)
        panel.isVisible = true

        Thread {

            if (!checkAdbConnect()) {
                content.text = jsonObject.get("connection_check_fail").asString
                exitButton(this, panel)
                return@Thread
            }

            val jProgressBar = JProgressBar()
            panel.add(jProgressBar)
            jProgressBar.isVisible = false
            jProgressBar.setBounds(10, 15, 260, 40)
            jProgressBar.maximum = 3
            jProgressBar.value = 0
            jProgressBar.isVisible = true

            title = jsonObject.get("connection_check_succeed").asString

            ControlCenter.getInstance().getControlListener().passAdbCheck()

            for (i: Int in 1..3) {
                content.text = String.format(jsonObject.get("time_remain").asString, 4 - i)
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    //
                }
                jProgressBar.value = i
            }
            dispose()
        }.start()
    }
}