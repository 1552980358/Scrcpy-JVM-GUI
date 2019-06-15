package com.skynight.scrcpy

import com.skynight.scrcpy.widgets.CheckBox
import java.awt.Toolkit
import javax.swing.JFrame

class ConnectionSelectDevice(list: List<String>): JFrame("选择设备") {
    private val CheckBoxList = mutableListOf<CheckBox>()

    init {
        list.size

        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(350, 350)
        isResizable = false
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = false

        for (i in list) {

        }

    }
}