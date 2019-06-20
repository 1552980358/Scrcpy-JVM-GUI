package com.skynight.scrcpy

import com.skynight.scrcpy.base.BaseIndex.Companion.PanelMarginRight
import com.skynight.scrcpy.base.BaseIndex.Companion.WidgetWithTextHeight
import com.skynight.scrcpy.base.GetConnectedDevices
import com.skynight.scrcpy.widgets.Button
import com.skynight.scrcpy.widgets.CheckBox
import com.skynight.scrcpy.widgets.Panel
import com.skynight.scrcpy.widgets.RadioButton
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import javax.swing.ButtonGroup
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JRadioButton

class SelectDeviceWindow(commands: MutableList<String>, single: Boolean = true): JFrame("选择设备") {
    private val checkBoxList = mutableListOf<CheckBox>()
    private val radioButtonList = mutableListOf<RadioButton>()

    init {
        val getConnectedDevices = GetConnectedDevices.getInstance()
        val list = getConnectedDevices.getDeviceList()
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(350,  list.size * WidgetWithTextHeight + 75)
        isResizable = false
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = false

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

        val mainPanel = Panel(height, width, null)
        add(mainPanel)

        var height = 0
        if (single) {
            val buttonGroup = ButtonGroup()
            for (i in list) {
                val radioButton = RadioButton(getConnectedDevices.getDeviceModel(i),i, list.indexOf(i) == 0)
                buttonGroup.add(radioButton)
                radioButtonList.add(radioButton)
                mainPanel.add(radioButton)
                //radioButton.setSize(width, WidgetWithTextHeight)
                radioButton.setBounds(0, height, width - 16, WidgetWithTextHeight)
                radioButton.horizontalAlignment = JRadioButton.CENTER
                height += WidgetWithTextHeight
            }
        } else {
            for (i in list) {
                val checkBox = CheckBox(getConnectedDevices.getDeviceModel(i), i)
                checkBoxList.add(checkBox)
                mainPanel.add(checkBox)
                //checkBox.setSize(width, WidgetWithTextHeight)
                checkBox.setBounds(0, height, width - 16, WidgetWithTextHeight)
                checkBox.horizontalAlignment = JCheckBox.CENTER
                height += WidgetWithTextHeight
            }
        }

        val button = Button("连接", 10, height, width - 20 - PanelMarginRight, 30)
        mainPanel.add(button)
        button.addActionListener {
            Thread {
                val commandList = mutableListOf<Array<String>>()
                if (single) {
                    startSingle@ for (i in radioButtonList) {
                        if (i.isSelected) {
                            commands.add("-s")
                            commands.add(i.deviceId)
                            commandList.add(commands.toTypedArray())
                            break@startSingle
                        }
                    }
                } else {
                    for (i in checkBoxList) {
                        if (i.isSelected) {
                            commands.add("-s")
                            commands.add(i.deviceId)
                            commandList.add(commands.toTypedArray())
                            commands.removeAt(commands.indexOf("-s"))
                            commands.removeAt(commands.indexOf(i.deviceId))
                        }
                    }
                }

                for (i in commandList) {
                    try {
                        println(i.toMutableList())
                        Runtime.getRuntime().exec(i)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }.start()
            dispose()
        }

        mainPanel.isVisible = true
        isVisible = true
    }


}