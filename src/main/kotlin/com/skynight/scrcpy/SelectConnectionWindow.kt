package com.skynight.scrcpy

import com.skynight.scrcpy.Base.ControlCenter
import com.skynight.scrcpy.widgets.Button
import com.skynight.scrcpy.widgets.Panel
import com.skynight.scrcpy.widgets.RadioButton
import java.awt.Toolkit
import javax.swing.*

class SelectConnectionWindow: JFrame("选择连接方式") {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(300, 230)
        isResizable = false
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE

        val jPanel = Panel(300, 200, null)
        add(jPanel)

        val singleDevice = Panel(0, 0, 300, 60)
        jPanel.add(singleDevice)
        singleDevice.border = BorderFactory.createTitledBorder("~ 单设备 ~")
        val buttonGroup = ButtonGroup()
        val singleWired = RadioButton("有线连接")
        buttonGroup.add(singleWired)
        singleDevice.add(singleWired)
        singleWired.isSelected = true
        val singleWireless = RadioButton("无线连接")
        buttonGroup.add(singleWireless)
        singleDevice.add(singleWireless)

        val multiDevice = Panel(0,60,300,90)
        jPanel.add(multiDevice)
        multiDevice.border = BorderFactory.createTitledBorder("~ 多设备 ~")
        val multiWired = RadioButton("有线连接(施工未开放)")
        buttonGroup.add(multiWired)
        multiDevice.add(multiWired)

        val multiWireless = RadioButton("无线连接(施工未开放)")
        buttonGroup.add(multiWireless)
        multiDevice.add(multiWireless)
        multiWireless.addItemListener {
            buttonGroup.clearSelection()
        }
        multiWired.addItemListener {
            buttonGroup.clearSelection()
        }
        val jButton = Button("确定", 10,150, 264, 40)
        jPanel.add(jButton)
        jButton.addActionListener {
            val controlCenter = ControlCenter.getInstance()
            controlCenter.isWiredMethod = singleWired.isSelected
            controlCenter.controlListener.onHandleConnectionMethod()
            dispose()
        }

        isVisible = true
    }
}