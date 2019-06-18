package com.skynight.scrcpy

import com.skynight.scrcpy.Base.ControlCenter
import com.skynight.scrcpy.Base.DecodeLanguagePack
import com.skynight.scrcpy.Base.GetConnectedDevices
import java.io.File
import java.io.FileWriter

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
    //test()

    DecodeLanguagePack.getInstance().setLocale().decode()
    /* Test Step */
    //SplashWindow()
    //ADBWirelessWindow()
    //ADBWiredWindow()
    //TestConnectionWindow()
    //MainWindow()
    //SelectDeviceWindow(false)

    //*
    ControlCenter.getInstance().controlListener = object : ControlListener {
        override fun passFileCheck() {
            super.passFileCheck()
            println("passFileCheck")
            //MainWindow()
            SelectConnectionWindow()
        }

        override fun onHandleConnectionMethod() {
            super.onHandleConnectionMethod()
            if (ControlCenter.getInstance().isWiredMethod) {
                ADBWiredWindow()
            } else {
                ADBWirelessWindow()
            }
        }

        override fun onConfirmConnection() {
            println("onConfirmConnection")
            TestConnectionWindow()
        }

        override fun passAdbCheck() {
            super.passAdbCheck()
            println("passAdbCheck")
            MainWindow.getInstance()
        }
    }

    // 启动
    SplashWindow()
    //*/
}

interface ControlListener {
    fun passFileCheck() {

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
    val file = File(System.getProperty("user.dir") + File.separator + DecodeLanguagePack.path)
    for (i in file.list()) {
        println(i)
    }

}