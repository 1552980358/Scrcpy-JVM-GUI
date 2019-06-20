package com.skynight.scrcpy

import com.google.gson.JsonObject
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.base.BaseIndex.Companion.WidgetWithTextHeight
import com.skynight.scrcpy.base.BaseIndex.Companion.BitRateList
import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.base.GetConnectedDevices
import com.skynight.scrcpy.widgets.CheckBox
import com.skynight.scrcpy.widgets.Panel
import com.skynight.scrcpy.widgets.RadioButton
import java.awt.CardLayout
import java.awt.Color
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JPanel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.ImageIcon
import javax.swing.ButtonGroup
import javax.swing.KeyStroke
import javax.swing.BorderFactory
import javax.swing.event.MenuEvent
import javax.swing.event.MenuListener

class MainWindow : JFrame() {
    private var bitRate = 0
    private val customBitRate = JTextField(3)
    @Suppress("PrivatePropertyName")
    private val CheckBoxes = mutableListOf<CheckBox>()
    private var jsonObject: JsonObject
    private val panelList = mutableMapOf<String, Panel>()
    private val mainPanel: JPanel
    private lateinit var deviceInfoPanel: Panel
    private lateinit var cardLayout: CardLayout
    @Suppress("PrivatePropertyName")
    private val DevicesMenu = mutableListOf<JMenuItem>()

    companion object {
        private var created = false
        fun isCreated(): Boolean {
            return created
        }
        @Volatile
        private var instance: MainWindow? = null
        @Synchronized
        fun getInstance(): MainWindow {
            if (instance == null) {
                created = true
                instance = MainWindow()
            }
            return instance as MainWindow
        }
    }

    override fun setVisible(b: Boolean) {
        if (!isVisible) {
            super.setVisible(b)
        }
    }

    init {
        Thread {
            try {
                Thread.sleep(3000)
            } catch (e: Exception) {

            }
            isVisible = true
        }.start()

        jsonObject = ControlCenter.getInstance().getLoadLanguage().getWindowStrings("MainWindow")

        val screenSize = Toolkit.getDefaultToolkit().screenSize

        title = jsonObject.get("title").asString
        setSize(750, 350)
        isResizable = false
        Thread { setMenu() }.start()
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = false
        iconImage = ImageIcon("icons/MainFrame.jpg").image

        mainPanel = Panel(width, height, null)
        add(mainPanel)
        contentPane = mainPanel
        mainPanel.isVisible = false

        Thread { setBitRate() }.start()
        Thread { setTools() }.start()
        Thread { getDeviceInfo() }.start()

        mainPanel.isVisible = true
    }

    private fun setMenu() {
        val jMenuBar = JMenuBar()
        setJMenuBar(jMenuBar)
        jMenuBar.background = Color.WHITE

        val menuObject = jsonObject.get("Menu").asJsonObject

        val about = JMenu(menuObject.get("about").asString)
        jMenuBar.add(about)
        about.setMnemonic('A')

        val deviceJMenu = JMenu(menuObject.get("devices").asString)
        deviceJMenu.mnemonic = KeyEvent.VK_D
        jMenuBar.add(deviceJMenu)
        val refresh = JMenuItem(menuObject.get("refresh").asString)
        deviceJMenu.add(refresh)
        refresh.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.SHIFT_MASK)
        refresh.background = Color.WHITE
        refresh.addActionListener {
            Thread {
                GetConnectedDevices.reloadConnectedDevices()
                mainPanel.remove(deviceInfoPanel)

                Thread { getDeviceInfo() }.start()

                Thread {
                    for (i in DevicesMenu) {
                        deviceJMenu.remove(i)
                    }
                    addDevicesToMenu(deviceJMenu)
                }.start()
            }.start()
        }
        try {
            addDevicesToMenu(deviceJMenu)
        } catch (e: Exception) {
            //
        }

        val connectNewDevice = JMenu(menuObject.get("newConnection").asString)
        connectNewDevice.mnemonic = KeyEvent.VK_N
        jMenuBar.add(connectNewDevice)
        connectNewDevice.addMenuListener(object : MenuListener {
            override fun menuSelected(e: MenuEvent?) {
                SelectConnectionWindow()
            }

            override fun menuCanceled(e: MenuEvent?) {
            }

            override fun menuDeselected(e: MenuEvent?) {
            }

        })

        val connect = JMenu(menuObject.get("scrcpy").asString)
        jMenuBar.add(connect)
        connect.mnemonic = KeyEvent.VK_S
        val connectDevice = JMenuItem(menuObject.get("single").asString)
        connectDevice.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK)
        connectDevice.background = Color.WHITE
        val connectDevices = JMenuItem(menuObject.get("multi").asString)
        connectDevices.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.SHIFT_MASK)
        connectDevices.background = Color.WHITE
        connect.add(connectDevice)
        connect.add(connectDevices)
        connectDevice.addActionListener {
            //onConnect()
            Thread {
                SelectDeviceWindow(onConnect())
                ControlKeyWindow.getInstance().showFrame()
            }.start()
        }
        connectDevices.addActionListener {
            Thread {
                SelectDeviceWindow(onConnect(), false)
                ControlKeyWindow.getInstance().showFrame()
            }.start()
        }
    }

    private fun onConnect(): MutableList<String> {

        val command = mutableListOf(
            "cmd.exe",
            "/c",
            "start",
            "cmd.exe",
            "/c",
            System.getProperty("user.dir") + File.separator + PackageFileList[6]
        )

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

        return command
    }

    private fun addDevicesToMenu(deviceJMenu: JMenu) {
        DevicesMenu.clear()
        val getConnectedDevices = GetConnectedDevices.getInstance()
        for (i in getConnectedDevices.getDeviceList()) {
            val jMenuItem = JMenuItem(getConnectedDevices.getDeviceModel(i))
            jMenuItem.background = Color.WHITE
            jMenuItem.addActionListener {
                cardLayout.show(deviceInfoPanel, i)
            }
            DevicesMenu.add(jMenuItem)
            deviceJMenu.add(jMenuItem)
        }
    }

    private fun getDeviceInfo() {
        val deviceInfo = jsonObject.get("DeviceInfo").asJsonObject
        try {
            mainPanel.remove(deviceInfoPanel)
            panelList.clear()
        } catch (e: Exception) {
            //
        }
        cardLayout = CardLayout()
        deviceInfoPanel = Panel(0, 0, width / 3, height - 64, cardLayout)
        mainPanel.add(deviceInfoPanel)
        deviceInfoPanel.border = BorderFactory.createTitledBorder(deviceInfo.get("title").asString)

        val getConnectedDevices = GetConnectedDevices.getInstance()
        for (i in getConnectedDevices.getDeviceList()) {

            val subPanel = Panel(0, 0, width / 3 - 16, 100, null)
            val b = Panel(0, 0, width / 3, 25)
            b.add(JLabel(deviceInfo.get("brand").asString))
            b.add(JLabel(getConnectedDevices.getDeviceBrand(i)))
            subPanel.add(b)

            val m = Panel(0, 25, width / 3 - 16, 25)
            m.add(JLabel(deviceInfo.get("model").asString))
            m.add(JLabel(getConnectedDevices.getDeviceModel(i)))
            subPanel.add(m)

            val a = Panel(0, 50, width / 3 - 16, 25)
            a.add(JLabel("Android "))
            a.add(JLabel(getConnectedDevices.getDeviceAndroidVersion(i)))
            subPanel.add(a)

            val s = Panel(0, 75, width / 3 - 16, 25)
            s.add(JLabel("SDK: "))
            s.add(JLabel(getConnectedDevices.getDeviceSDK(i)))
            subPanel.add(s)

            val imei = StringBuilder(getConnectedDevices.getDeviceImei(i))
            for (j: Int in 0..imei.lastIndex) {
                if (j >= imei.lastIndex / 2 - imei.lastIndex / 4 && j <= imei.lastIndex / 2 + imei.lastIndex / 4) {
                    imei[j] = '*'
                }
            }

            val im = Panel(0, 100, width / 3 - 16, 25)
            im.add(JLabel("IMEI: "))
            im.add(JLabel(imei.toString()))
            subPanel.add(im)

            val id = Panel(0, 125, width / 3 - 16, 25)
            id.add(JLabel(deviceInfo.get("adb_id").asString))
            val adbId = JTextField(i)
            adbId.isEditable = false
            id.add(adbId)
            subPanel.add(id)

            val status = Panel(0, 150, width / 3 - 16, 25)
            status.add(JLabel(deviceInfo.get("status").asString))
            val jLabel = JLabel()
            jLabel.background = Color.BLACK
            val st = deviceInfo.get("statuses").asJsonArray
            jLabel.text = when (getConnectedDevices.getDeviceState(i)) {
                "device" -> {
                    jLabel.foreground = Color.GREEN
                    //"在线"
                    st[0].asString
                }
                "offline" -> {
                    jLabel.foreground = Color.ORANGE
                    //"离线"
                    st[1].asString
                }
                "unauthorize" -> {
                    jLabel.foreground = Color.BLUE
                    //"未验证"
                    st[2].asString
                }
                else -> {
                    jLabel.foreground = Color.RED
                    //"已断开"
                    st[3].asString
                }
            }
            status.add(jLabel)
            subPanel.add(status)

            val type = Panel(0, 175, width / 3 - 16, 25)
            type.add(JLabel(deviceInfo.get("type").asString))
            type.add(
                JLabel(
                    (if (!i.startsWith("192.168"))
                        deviceInfo.get("wired")
                    else deviceInfo.get(
                        "wireless"
                    )).asString
                )
            )
            subPanel.add(type)

            panelList[i] = subPanel

            deviceInfoPanel.add(subPanel, i)
        }
    }

    private fun setBitRate() {
        @Suppress("LocalVariableName") val BitRate = jsonObject.get("BitRate").asJsonObject
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createTitledBorder(BitRate.get("title").asString)
        mainPanel.add(jPanel)
        jPanel.background = Color.WHITE
        jPanel.setBounds(width / 3, 0, width / 3, 120)
        jPanel.layout = null

        val bitRatePanel1 = JPanel()
        bitRatePanel1.isVisible = false
        bitRatePanel1.background = Color.WHITE
        bitRatePanel1.setBounds(10, 20, 230, WidgetWithTextHeight)
        jPanel.add(bitRatePanel1)

        val buttonGroup = ButtonGroup()

        val bitRate1 = RadioButton(BitRate.get("default").asString)
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

        val bitRate5 = RadioButton(BitRate.get("custom").asString)
        buttonGroup.add(bitRate5)
        bitRatePanel3.add(bitRate5)
        bitRate5.background = Color.WHITE
        bitRate5.addActionListener {
            bitRate = 4
        }
        bitRatePanel3.add(customBitRate)
        //customBitRate.setSize(100, 26)
        bitRatePanel3.isVisible = true
    }

    private fun setTools() {
        val tools = jsonObject.get("Tools").asJsonObject
        val jPanel = JPanel()
        jPanel.border = BorderFactory.createTitledBorder(tools.get("title").asString)
        mainPanel.add(jPanel)
        jPanel.setBounds(width / 3, 120, width / 3, 60)
        jPanel.background = Color.WHITE
        jPanel.setSize(250, 120)
        jPanel.layout = null

        val jPanel1 = JPanel()
        jPanel.add(jPanel1)
        jPanel1.background = Color.WHITE
        jPanel1.setBounds(10, 20, 230, WidgetWithTextHeight)

        CheckBoxes.add(CheckBox(tools.get("fullscreen").asString, false, 0))
        jPanel1.add(CheckBoxes[CheckBoxes.lastIndex])

        CheckBoxes.add(CheckBox(tools.get("touch_shown").asString, false, 3))
        jPanel1.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel1.isVisible = true

        val jPanel2 = JPanel()
        jPanel.add(jPanel2)
        jPanel2.background = Color.WHITE
        jPanel2.setBounds(10, 46, 230, WidgetWithTextHeight)

        CheckBoxes.add(CheckBox(tools.get("control").asString, true, 1))
        jPanel2.add(CheckBoxes[CheckBoxes.lastIndex])

        CheckBoxes.add(CheckBox(tools.get("frame_treat").asString, false, 4))
        jPanel2.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel2.isVisible = true

        val jPanel3 = JPanel()
        jPanel.add(jPanel3)
        jPanel3.background = Color.WHITE
        jPanel3.setBounds(10, 72, 230, WidgetWithTextHeight)

        CheckBoxes.add(CheckBox(tools.get("keep_front").asString, false, 2))
        jPanel3.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel3.isVisible = true
    }
}