package com.skynight.scrcpy

import com.skynight.scrcpy.Base.ControlCenter
import com.skynight.scrcpy.Base.checkAdbConnect
import com.skynight.scrcpy.Base.exitButton
import java.awt.Color
import java.awt.Toolkit
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar

class TestConnection : JFrame("启动中, 请稍后...") {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(300, 120)
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true
        val panel = JPanel()
        add(panel)
        panel.background = Color.WHITE
        panel.layout = null
        panel.isVisible = false
        panel.setSize(width, height)

        val content = JLabel("检查设备连接...", JLabel.CENTER)
        content.setBounds(0, 0, width - 16, 15)
        panel.add(content)
        panel.isVisible = true

        Thread {

            if (!checkAdbConnect()) {
                content.text = "无法检测连接!"
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

            title = "检查完成, 等待主程序加载..."

            ControlCenter.getInstance().controlListener.passAdbCheck()

            for (i: Int in 1..3) {
                content.text = "还剩下${4 - i}秒"
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