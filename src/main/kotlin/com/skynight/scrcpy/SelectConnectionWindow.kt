package com.skynight.scrcpy

import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.base.LoadLanguage
import com.skynight.scrcpy.widgets.Button
import com.skynight.scrcpy.widgets.Panel
import com.skynight.scrcpy.widgets.RadioButton
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.*

class SelectConnectionWindow: JFrame() {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val loadLanguage = LoadLanguage.instance.getWindowStrings("SelectConnectionWindow")

        title = loadLanguage.get("title").asString
        setSize(300, 160)
        isResizable = false
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = if (MainWindow.isCreated()) JFrame.DISPOSE_ON_CLOSE else JFrame.EXIT_ON_CLOSE
        isAlwaysOnTop = true
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

        val jPanel = Panel(width, height, null)
        add(jPanel)

        val buttonGroup = ButtonGroup()
        val wired = RadioButton(loadLanguage.get("wired").asString)
        buttonGroup.add(wired)
        jPanel.add(wired)
        wired.isSelected = true
        wired.setBounds(0,10, 284, 30)
        wired.horizontalAlignment = JRadioButton.CENTER
        val wireless = RadioButton(loadLanguage.get("wireless").asString)
        buttonGroup.add(wireless)
        jPanel.add(wireless)
        wireless.horizontalAlignment = JRadioButton.CENTER
        wireless.setBounds(0,40, 284, 30)

        val jButton = Button(loadLanguage.get("confirm").asString, 10,70, 264, 40)
        jPanel.add(jButton)
        jButton.addActionListener {
            val controlCenter = ControlCenter.instance
            controlCenter.isWiredMethod = wired.isSelected
            controlCenter.getControlListener().onHandleConnectionMethod()
            dispose()
        }

        isVisible = true
    }
}