package com.skynight.scrcpy

import com.skynight.scrcpy.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.BaseIndex.Companion.WidgetWithTextHeight
import com.skynight.scrcpy.BaseIndex.Companion.BitRateList
import com.skynight.scrcpy.widgets.CheckBox
import com.skynight.scrcpy.widgets.RadioButton
import java.awt.Color
import java.awt.Toolkit
import java.io.File
import javax.swing.*

class MainWindow : JFrame("Scrcpy - JVM GUI") {
    private var bitRate = 0
    private val customBitRate = JTextField(2)
    @Suppress("PrivatePropertyName")
    private val CheckBoxes = mutableListOf<CheckBox>()

    init {
        Thread {
            try {
                Thread.sleep(3200)
            } catch (e: Exception) {

            }
            isVisible = true
        }.start()

        val screenSize = Toolkit.getDefaultToolkit().screenSize

        setSize(750, 350)
        isResizable = false
        setMenu()
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = false
        iconImage = ImageIcon("icons/MainFrame.jpg").image

        val jPanel = JPanel()
        add(jPanel)
        contentPane = jPanel
        jPanel.background = Color.WHITE
        jPanel.isVisible = false
        jPanel.layout = null
        jPanel.setSize(750, 350)

        setBitRate(jPanel)
        setTools(jPanel)

        jPanel.isVisible = true
    }

    private fun setMenu() {
        val jMenuBar = JMenuBar()
        setJMenuBar(jMenuBar)
        jMenuBar.background = Color.WHITE

        val about = JMenu("关于")
        jMenuBar.add(about)
        val device = JMenu("设备")
        val busying = JMenuItem(if (ControlCenter.getInstance().isWiredMethod) "USB有线连接" else "TCP/IP无线连接")
        device.add(busying)
        jMenuBar.add(device)

        val connect = JMenu("开始投屏")
        jMenuBar.add(connect)
        val connectDevice = JMenuItem("单设备投屏")
        val connectDevices = JMenuItem("多设备同时投屏(施工未开放)")
        connect.add(connectDevice)
        connect.add(connectDevices)
        connectDevice.addActionListener {
            onConnect()
        }
    }

    private fun onConnect() {
        val command = mutableListOf(
            "cmd.exe",
            "/c",
            "start",
            "cmd.exe",
            "/c",
            System.getProperty("user.dir") + File.separator + PackageFileList[6]
        )
        val adbDevicesRes = runAdbGetList("deices")

        val adbDevices = mutableListOf<String>()
        for (i: Int in 1 until adbDevicesRes.size - 1) {
            val device = StringBuilder()
            for (j in adbDevicesRes[i]) {
                if (j.toString() == "\t") {
                    break
                }
                device.append(j)
            }
            adbDevices.add(device.toString())
        }

        for (i in CheckBoxes) {
            command.add(i.getArg())
        }

        val bitrate = when (bitRate) {
            0 -> ""
            4 -> {
                "-b ${customBitRate}M"
            }
            else -> "-b ${BitRateList[bitRate]}M"
        }
        command.add(bitrate)

        Runtime.getRuntime().exec(command.toTypedArray())
    }

    private fun setBitRate(mainPanel: JPanel) {
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createTitledBorder("~ 投屏比特率 ~")
        mainPanel.add(jPanel)
        jPanel.background = Color.WHITE
        jPanel.setSize(250, 120)
        jPanel.layout = null

        val bitRatePanel1 = JPanel()
        bitRatePanel1.isVisible = false
        bitRatePanel1.background = Color.WHITE
        bitRatePanel1.setBounds(10, 20, 230, WidgetWithTextHeight)
        jPanel.add(bitRatePanel1)

        val buttonGroup = ButtonGroup()

        val bitRate1 = RadioButton("8M (默认)")
        buttonGroup.add(bitRate1)
        bitRate1.isSelected = true
        bitRatePanel1.add(bitRate1)
        bitRate1.background = Color.WHITE
        bitRate1.addActionListener {
            bitRate = 0
        }
        val bitRate2 = RadioButton("30M")
        buttonGroup.add(bitRate2)
        bitRatePanel1.add(bitRate2)
        bitRate2.background = Color.WHITE
        bitRate2.addActionListener {
            bitRate = 1
        }
        bitRatePanel1.isVisible = true

        val bitRatePanel2 = JPanel()
        bitRatePanel2.background = Color.WHITE
        bitRatePanel2.isVisible = false
        bitRatePanel2.setBounds(10, 46, 230, WidgetWithTextHeight)
        jPanel.add(bitRatePanel2)

        val bitRate3 = RadioButton("20M")
        buttonGroup.add(bitRate3)
        bitRatePanel2.add(bitRate3)
        bitRate3.background = Color.WHITE
        bitRate3.addActionListener {
            bitRate = 2
        }
        val bitRate4 = RadioButton("4M")
        buttonGroup.add(bitRate4)
        bitRatePanel2.add(bitRate4)
        bitRate4.background = Color.WHITE
        bitRate4.addActionListener {
            bitRate = 3
        }
        bitRatePanel2.isVisible = true

        val bitRatePanel3 = JPanel()
        jPanel.add(bitRatePanel3)
        bitRatePanel3.background = Color.WHITE
        bitRatePanel3.isVisible = false
        bitRatePanel3.setBounds(10, 72, 230, WidgetWithTextHeight)
        //bitRatePanel3.layout = null

        val bitRate5 = RadioButton("自定义(单位M)")
        buttonGroup.add(bitRate5)
        bitRatePanel3.add(bitRate5)
        bitRate5.background = Color.WHITE
        bitRate5.addActionListener {
            bitRate = 4
        }
        bitRatePanel3.add(customBitRate)
        customBitRate.setSize(100, WidgetWithTextHeight)
        bitRatePanel3.isVisible = true
    }

    private fun setTools(mainPanel: JPanel) {
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createTitledBorder("~ 附加选项 ~")
        mainPanel.add(jPanel)
        jPanel.setBounds(0, 120, 250, 60)
        jPanel.background = Color.WHITE
        jPanel.setSize(250, 120)
        jPanel.layout = null

        val jPanel1 = JPanel()
        jPanel.add(jPanel1)
        jPanel1.background = Color.WHITE
        jPanel1.setBounds(10, 20, 230, WidgetWithTextHeight)

        CheckBoxes.add(CheckBox("全屏显示", false, 0))
        jPanel1.add(CheckBoxes[CheckBoxes.lastIndex])

        CheckBoxes.add(CheckBox("触摸显示", false, 3))
        jPanel1.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel1.isVisible = true

        val jPanel2 = JPanel()
        jPanel.add(jPanel2)
        jPanel2.background = Color.WHITE
        jPanel2.setBounds(10, 46, 230, WidgetWithTextHeight)

        CheckBoxes.add(CheckBox("鼠标操控", true, 1))
        jPanel2.add(CheckBoxes[CheckBoxes.lastIndex])

        CheckBoxes.add(CheckBox("显示帧数", false, 4))
        jPanel2.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel2.isVisible = true

        val jPanel3 = JPanel()
        jPanel.add(jPanel3)
        jPanel3.background = Color.WHITE
        jPanel3.setBounds(10, 72, 230, WidgetWithTextHeight)

        CheckBoxes.add(CheckBox("保持前端", false, 2))
        jPanel3.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel3.isVisible = true
    }
}