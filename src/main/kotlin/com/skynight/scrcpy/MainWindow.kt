package com.skynight.scrcpy

import com.google.gson.JsonObject
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFileList
import com.skynight.scrcpy.base.BaseIndex.Companion.WidgetWithTextHeight
import com.skynight.scrcpy.base.BaseIndex.Companion.BitRateList
import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.base.GetConnectedDevices
import com.skynight.scrcpy.base.LoadLanguage
import com.skynight.scrcpy.widgets.*
import java.awt.CardLayout
import java.awt.Color
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.io.File
import java.util.*
import javax.swing.*
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
    private val DevicesMenu = mutableListOf<MenuItem>()

    companion object {
        private var created = false
        fun isCreated(): Boolean {
            return created
        }

        val instance: MainWindow by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            created = true
            MainWindow()
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
                Thread.sleep(3200)
            } catch (e: Exception) {

            }
            isVisible = true
        }.start()

        jsonObject = LoadLanguage.instance.getWindowStrings("MainWindow")

        val screenSize = Toolkit.getDefaultToolkit().screenSize

        title = jsonObject.get("title").asString
        setSize(750, 304)
        isResizable = false
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = false
        iconImage = ImageIcon("icons/MainFrame.jpg").image

        mainPanel = Panel(width, height, null)
        add(mainPanel)
        contentPane = mainPanel
        mainPanel.isVisible = false

        LogOutputWindow.takeLog(" - MainWindow - ").newLine()
        Thread {
            LogOutputWindow.takeLog("Set Menu Start")
            setMenu()
        }.start()
        Thread {
            LogOutputWindow.takeLog("Set BitRate Start")
            setBitRate()
        }.start()
        Thread {
            LogOutputWindow.takeLog("Set Tools Start")
            setTools()
        }.start()
        Thread {
            LogOutputWindow.takeLog("Set Device Info Start")
            getDeviceInfo()
        }.start()
        Thread {
            LogOutputWindow.takeLog("Get Saved Load Start")
            getSaveLoad()
        }.start()

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
        val properties = Properties()
        properties.load(MainWindow::class.java.classLoader.getResourceAsStream("version.properties"))
        val version = MenuItem(menuObject.get("version").asString + properties.getProperty("version"))
        about.add(version)

        val deviceJMenu = JMenu(menuObject.get("devices").asString)
        deviceJMenu.mnemonic = KeyEvent.VK_D
        jMenuBar.add(deviceJMenu)
        val refresh = MenuItem(menuObject.get("refresh").asString)
        deviceJMenu.add(refresh)
        refresh.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.SHIFT_MASK)
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
            LogOutputWindow.takeLog(e)
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
        val connectDevice = MenuItem(menuObject.get("single").asString)
        connectDevice.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK)
        val connectDevices = MenuItem(menuObject.get("multi").asString)
        connectDevices.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.SHIFT_MASK)
        connect.add(connectDevice)
        connect.add(connectDevices)
        connectDevice.addActionListener {
            //onConnect()
            Thread {
                SelectDeviceWindow(onConnect())
                if (ControlCenter.instance.getTips()) {
                    ControlKeyWindow.instance.showFrame()
                }
            }.start()
        }
        connectDevices.addActionListener {
            Thread {
                SelectDeviceWindow(onConnect(), false)
                if (ControlCenter.instance.getTips()) {
                    ControlKeyWindow.instance.showFrame()
                }
            }.start()
        }

        val chooseColor = JMenu("自定义颜色")
        jMenuBar.add(chooseColor)
        val bgColor = MenuItem("背景颜色")
        chooseColor.add(bgColor)
        bgColor.addActionListener {
            ControlCenter.instance.setBGColor(
                JColorChooser.showDialog(
                    this,
                    "背景颜色",
                    ControlCenter.instance.getBGColor()
                )
            )
        }
        val fgColor = MenuItem("文字颜色")
        chooseColor.add(fgColor)
        fgColor.addActionListener {
            ControlCenter.instance.setFGColor(
                JColorChooser.showDialog(
                    this,
                    "文字颜色",
                    ControlCenter.instance.getFGColor()
                )
            )
        }

    }

    private fun onConnect(): MutableList<String> {

        val command = if (ControlCenter.instance.getConsoleless()) {
            mutableListOf(System.getProperty("user.dir") + File.separator + PackageFileList[7])
        } else {
            mutableListOf(
                "cmd.exe",
                "/c",
                "start",
                "cmd.exe",
                "/c",
                System.getProperty("user.dir") + File.separator + PackageFileList[6]
            )
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

        LogOutputWindow.takeLog(command.toString())

        return command
    }

    private fun addDevicesToMenu(deviceJMenu: JMenu) {
        DevicesMenu.clear()
        val getConnectedDevices = GetConnectedDevices.getInstance()
        for (i in getConnectedDevices.getDeviceList()) {
            LogOutputWindow.takeLog("AddDevicesToMenu: $i")
            val menuItem = MenuItem(getConnectedDevices.getDeviceModel(i))
            menuItem.addActionListener {
                cardLayout.show(deviceInfoPanel, i)
            }
            DevicesMenu.add(menuItem)
            deviceJMenu.add(menuItem)
        }
    }

    private fun getSaveLoad() {
        val saveLoad = jsonObject.get("SaveLoad").asJsonObject
        val panel = Panel(width * 2 / 3, 0, width / 3 - 16, 120)
        mainPanel.add(panel)
        panel.border = BorderFactory.createTitledBorder(saveLoad.get("title").asString)
        val logOutputWindow =
            CheckBox(saveLoad.get("LogOutputWindow").asString, ControlCenter.instance.getLogOutputWindow())
        panel.add(logOutputWindow)
        logOutputWindow.addItemListener {
            ControlCenter.instance.setLogOutputWindow(logOutputWindow.isSelected)
        }
        LogOutputWindow.instance.listener = object : LogWindowOperationListener {
            override fun onStateChange(isVisible: Boolean) {
                super.onStateChange(isVisible)
                logOutputWindow.isSelected = isVisible
            }
        }

        val consoleless = CheckBox(saveLoad.get("Consoleless").asString, ControlCenter.instance.getConsoleless())
        panel.add(consoleless)
        consoleless.addActionListener {
            ControlCenter.instance.setConsoleless(consoleless.isSelected)
        }

        val tips = CheckBox(saveLoad.get("Tips").asString, ControlCenter.instance.getTips())
        panel.add(tips)
        tips.addActionListener {
            ControlCenter.instance.setTips(tips.isSelected)
        }
    }

    private fun getDeviceInfo() {
        val deviceInfo = jsonObject.get("DeviceInfo").asJsonObject
        if (::deviceInfoPanel.isInitialized) {
            mainPanel.remove(deviceInfoPanel)
            panelList.clear()
        }
        cardLayout = CardLayout()
        deviceInfoPanel = Panel(0, 0, width / 3, 240, cardLayout)
        mainPanel.add(deviceInfoPanel)
        deviceInfoPanel.border = BorderFactory.createTitledBorder(deviceInfo.get("title").asString)

        val getConnectedDevices = GetConnectedDevices.getInstance()
        for (i in getConnectedDevices.getDeviceList()) {

            val subPanel = Panel(0, 0, width / 3 - 16, 100, null)
            val b = Panel(0, 0, width / 3, 25)
            b.add(Label(deviceInfo.get("brand").asString))
            b.add(Label(getConnectedDevices.getDeviceBrand(i)))
            subPanel.add(b)

            val m = Panel(0, 25, width / 3 - 16, 25)
            m.add(Label(deviceInfo.get("model").asString))
            m.add(Label(getConnectedDevices.getDeviceModel(i)))
            subPanel.add(m)

            val a = Panel(0, 50, width / 3 - 16, 25)
            a.add(Label("Android "))
            a.add(Label(getConnectedDevices.getDeviceAndroidVersion(i)))
            subPanel.add(a)

            val s = Panel(0, 75, width / 3 - 16, 25)
            s.add(Label("SDK: "))
            s.add(Label(getConnectedDevices.getDeviceSDK(i)))
            subPanel.add(s)

            val imei = StringBuilder(getConnectedDevices.getDeviceImei(i))
            for (j: Int in 0..imei.lastIndex) {
                if (j >= imei.lastIndex / 2 - imei.lastIndex / 4 && j <= imei.lastIndex / 2 + imei.lastIndex / 4) {
                    imei[j] = '*'
                }
            }

            val im = Panel(0, 100, width / 3 - 16, 25)
            im.add(Label("IMEI: "))
            im.add(Label(imei.toString()))
            subPanel.add(im)

            val id = Panel(0, 125, width / 3 - 16, 25)
            id.add(Label(deviceInfo.get("adb_id").asString))
            val adbId = JTextField(i)
            adbId.isEditable = false
            id.add(adbId)
            subPanel.add(id)

            val status = Panel(0, 150, width / 3 - 16, 25)
            status.add(Label(deviceInfo.get("status").asString))
            val label = Label()
            label.background = Color.BLACK
            val st = deviceInfo.get("statuses").asJsonArray
            label.text = when (getConnectedDevices.getDeviceState(i)) {
                "device" -> {
                    label.foreground = Color.GREEN
                    //"在线"
                    st[0].asString
                }
                "offline" -> {
                    label.foreground = Color.ORANGE
                    //"离线"
                    st[1].asString
                }
                "unauthorize" -> {
                    label.foreground = Color.BLUE
                    //"未验证"
                    st[2].asString
                }
                else -> {
                    label.foreground = Color.RED
                    //"已断开"
                    st[3].asString
                }
            }
            status.add(label)
            subPanel.add(status)

            val type = Panel(0, 175, width / 3 - 16, 25)
            type.add(Label(deviceInfo.get("type").asString))
            type.add(
                Label(
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
        val jPanel = Panel(width / 3, 0, width / 3, 120, null)
        jPanel.border = BorderFactory.createTitledBorder(BitRate.get("title").asString)
        mainPanel.add(jPanel)

        val bitRatePanel1 = Panel(10, 20, 230, WidgetWithTextHeight)
        bitRatePanel1.isVisible = false
        jPanel.add(bitRatePanel1)

        val buttonGroup = ButtonGroup()

        val bitRate1 = RadioButton(BitRate.get("default").asString)
        buttonGroup.add(bitRate1)
        bitRate1.isSelected = true
        bitRatePanel1.add(bitRate1)
        bitRate1.addActionListener {
            bitRate = 0
        }
        val bitRate2 = RadioButton("30M")
        buttonGroup.add(bitRate2)
        bitRatePanel1.add(bitRate2)
        bitRate2.addActionListener {
            bitRate = 1
        }
        bitRatePanel1.isVisible = true

        val bitRatePanel2 = Panel(10, 46, 230, WidgetWithTextHeight)
        bitRatePanel2.isVisible = false
        jPanel.add(bitRatePanel2)

        val bitRate3 = RadioButton("20M")
        buttonGroup.add(bitRate3)
        bitRatePanel2.add(bitRate3)
        bitRate3.addActionListener {
            bitRate = 2
        }
        val bitRate4 = RadioButton("4M")
        buttonGroup.add(bitRate4)
        bitRatePanel2.add(bitRate4)
        bitRate4.addActionListener {
            bitRate = 3
        }
        bitRatePanel2.isVisible = true

        val bitRatePanel3 = Panel(10, 72, 230, WidgetWithTextHeight)
        jPanel.add(bitRatePanel3)
        bitRatePanel3.isVisible = false

        val bitRate5 = RadioButton(BitRate.get("custom").asString)
        buttonGroup.add(bitRate5)
        bitRatePanel3.add(bitRate5)
        bitRate5.addActionListener {
            bitRate = 4
        }
        bitRatePanel3.add(customBitRate)
        bitRatePanel3.isVisible = true
    }

    private fun setTools() {
        val tools = jsonObject.get("Tools").asJsonObject
        val panel = Panel(width / 3, 120, width / 3, 120, null)
        panel.border = BorderFactory.createTitledBorder(tools.get("title").asString)
        mainPanel.add(panel)

        val jPanel1 = Panel(10, 20, 230, WidgetWithTextHeight)
        panel.add(jPanel1)

        CheckBoxes.add(CheckBox(tools.get("fullscreen").asString, false, 0))
        jPanel1.add(CheckBoxes[CheckBoxes.lastIndex])

        CheckBoxes.add(CheckBox(tools.get("touch_shown").asString, false, 3))
        jPanel1.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel1.isVisible = true

        val jPanel2 = Panel(10, 46, 230, WidgetWithTextHeight)
        panel.add(jPanel2)

        CheckBoxes.add(CheckBox(tools.get("control").asString, true, 1))
        jPanel2.add(CheckBoxes[CheckBoxes.lastIndex])

        CheckBoxes.add(CheckBox(tools.get("frame_treat").asString, false, 4))
        jPanel2.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel2.isVisible = true

        val jPanel3 = Panel(10, 72, 230, WidgetWithTextHeight)
        panel.add(jPanel3)

        CheckBoxes.add(CheckBox(tools.get("keep_front").asString, false, 2))
        jPanel3.add(CheckBoxes[CheckBoxes.lastIndex])

        jPanel3.isVisible = true
    }
}