package com.skynight.scrcpy

import com.skynight.scrcpy.Base.BaseIndex.Companion.ControlKeyFunctionList
import com.skynight.scrcpy.Base.BaseIndex.Companion.ControlKeyList
import com.skynight.scrcpy.widgets.Panel
import java.awt.Color
import java.awt.Toolkit
import javax.swing.*

class ControlKeyWindow private constructor(): JFrame("食用指南") {
    companion object {
        private var instance: ControlKeyWindow? = null
        @Synchronized
        fun getInstance(): ControlKeyWindow {
            if (instance == null) {
                instance = ControlKeyWindow()
            }
            return instance as ControlKeyWindow
        }
    }

    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setSize(500, 515)
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        val jPanel = Panel(width, height, null)
        add(jPanel)

        for (i in ControlKeyFunctionList) {
            val subPanel = Panel(
                ControlKeyFunctionList.indexOf(i) % 2 * 250,
                ControlKeyFunctionList.indexOf(i) / 2 * 50,
                width / 2,
                50
            )
            jPanel.add(subPanel)
            subPanel.border = BorderFactory.createTitledBorder("~ $i ~")

            val jLabel = JLabel(ControlKeyList[i])
            subPanel.add(jLabel)

            subPanel.isVisible = true
        }
        setMenus()
        jPanel.isVisible = true
    }

    private fun setMenus() {
        val jMenuBar = JMenuBar()
        setJMenuBar(jMenuBar)

        jMenuBar.background = Color.WHITE

        val shutdown = JMenu("关机")
        jMenuBar.add(shutdown)
        val reboot = JMenu("重启")
        jMenuBar.add(reboot)
    }

    fun showFrame() {
        if (!isVisible) {
            isVisible = true
        }
    }

}