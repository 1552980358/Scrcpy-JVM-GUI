package com.skynight.scrcpy

import com.skynight.scrcpy.base.ControlCenter
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileInputStream
import java.util.*

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
    //test()
    //LoadJson.getInstance()
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
    ControlCenter.getInstance().setControlListener(object : ControlListener {
        override fun checkUserSave(splash: Boolean) {
            super.checkUserSave(splash)
            //println("checkUserSave  $splash")
            LogOutputWindow.takeLog("checkUserSave  $splash")
            if (splash) {
                SplashWindow()
            } else {
                SelectLanguageWindow()
            }
        }

        override fun passFileCheck() {
            super.passFileCheck()
            //println("passFileCheck")
            LogOutputWindow.takeLog("passFileCheck")
            SelectConnectionWindow()
        }

        override fun onHandleConnectionMethod() {
            super.onHandleConnectionMethod()
            if (ControlCenter.getInstance().isWiredMethod) {
                LogOutputWindow.takeLog("onHandleConnectionMethod Wired")
                ADBWiredWindow()
            } else {
                LogOutputWindow.takeLog("onHandleConnectionMethod Wireless")
                ADBWirelessWindow()
            }
        }

        override fun onConfirmConnection() {
            super.onConfirmConnection()
            //println("onConfirmConnection")
            LogOutputWindow.takeLog("onConfirmConnection")
            TestConnectionWindow()
        }
        override fun passAdbCheck() {
            super.passAdbCheck()
            //println("passAdbCheck")
            LogOutputWindow.takeLog("passAdbCheck")
            MainWindow.instance
        }
    })

    //LoadJson.getInstance()

    // 启动
    //SplashWindow()
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
/*
    val locale = Locale.getDefault()
    println(locale.language)
    println(locale.country)
*/

    val path = System.getProperty("user.dir") + File.separator + "package"
    for (i in File(path).list()) {
        val f = File(path + File.separator + i)
        println(f.path)
        println(DigestUtils.md5Hex(FileInputStream(f)))
    }

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