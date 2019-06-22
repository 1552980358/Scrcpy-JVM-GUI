package com.skynight.scrcpy

import com.skynight.scrcpy.base.BaseIndex
import com.skynight.scrcpy.base.ControlCenter
import java.io.File

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
    //test()
    //SelectLanguageWindow()

    /* Unit Test */
    //LogOutputWindow.getInstance()
    //SplashWindow()
    //ADBWirelessWindow()
    //ADBWiredWindow()
    //TestConnectionWindow()
    //MainWindow()
    //SelectDeviceWindow(false)

    //*
    ControlCenter.instance.setControlListener(object : ControlListener {
        override fun checkUserSave(splash: Boolean) {
            super.checkUserSave(splash)
            LogOutputWindow.takeLog("Launch Procee: CheckUserSave  $splash").newLine()
            if (splash) {
                SplashWindow()
            } else {
                SelectLanguageWindow()
            }
        }

        override fun passFileCheck() {
            super.passFileCheck()
            LogOutputWindow.takeLog("Launch Procee: PassFileCheck").newLine()
            SelectConnectionWindow()
        }

        override fun onHandleConnectionMethod() {
            super.onHandleConnectionMethod()
            if (ControlCenter.instance.isWiredMethod) {
                LogOutputWindow.takeLog("Launch Procee: OnHandleConnectionMethod Wired").newLine()
                ADBWiredWindow()
            } else {
                LogOutputWindow.takeLog("Launch Procee: OnHandleConnectionMethod Wireless").newLine()
                ADBWirelessWindow()
            }
        }

        override fun onConfirmConnection() {
            super.onConfirmConnection()
            LogOutputWindow.takeLog("Launch Procee: OnConfirmConnection").newLine()
            TestConnectionWindow()
        }
        override fun passAdbCheck() {
            super.passAdbCheck()
            LogOutputWindow.takeLog("Launch Procee: PassAdbCheck").newLine()
            MainWindow.instance
        }
    })
    //*/
}

interface ControlListener {
    fun passFileCheck() {

    }

    fun checkUserSave(splash: Boolean) {

    }

    fun onHandleConnectionMethod() {

    }

    fun onConfirmConnection() {

    }

    fun passAdbCheck() {

    }

}

/* Test Program Fun */
@Suppress("unused")
fun test() {

    Runtime.getRuntime().exec(System.getProperty("user.dir") + File.separator + BaseIndex.PackageFileList[7])

/*
    val locale = Locale.getDefault()
    println(locale.language)
    println(locale.country)
*/
/*
    val path = System.getProperty("user.dir") + File.separator + "package"
    for (i in File(path).list()) {
        val f = File(path + File.separator + i)
        println(f.path)
        println(DigestUtils.md5Hex(FileInputStream(f)))
    }
*/
    /*
    if (SystemTray.isSupported()) {
        val systemTray = SystemTray.getSystemTray()
        val trayIcon = TrayIcon(
            Toolkit.getDefaultToolkit().createImage("icons/MainFrame.jpg"),
            "嘤嘤嘤"
        )
        trayIcon.isImageAutoSize = true
        trayIcon.toolTip = "这里是状态栏提示"
        systemTray.add(trayIcon)
        trayIcon.displayMessage(
            "这里是标题",
            "狐狸瑟瑟发抖",
            TrayIcon.MessageType.INFO
        )

    }

     */
    /*
    GetConnectedDevices.reGetConnectedDevices()
    for (i in GetConnectedDevices.getInstance().getDeviceList()) {
        println("$i = ${GetConnectedDevices.getInstance().getDeviceStateList()[i]}")
        println(GetConnectedDevices.getInstance().getDeviceBrand(i))
        println(GetConnectedDevices.getInstance().getDeviceModel(i))
        println(GetConnectedDevices.getInstance().getDeviceSDK(i))
        println(GetConnectedDevices.getInstance().getDeviceImei(i))
    }
*/
    /*
    val file = File(System.getProperty("user.dir") + File.separator + LoadJson.path)
    for (i in file.list()) {
        println(i)
    }

     */
}