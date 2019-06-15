package com.skynight.scrcpy

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
   //test()

    //*
    val controlListener = object : ControlListener {
        override fun passFileCheck() {
            super.passFileCheck()
            println("passFileCheck")
            //ConnectionSelectDevice(listOf("192.168.1.100:5555"))
            MainWindow(true)
            //ADBWiredConnection(this)
            //ADBWirelessConnection(this)
        }

        override fun onConfirmConnection(wired: Boolean) {
            super.onConfirmConnection(wired)
            println("onConfirmConnection")
            Connection(this, wired)
        }

        override fun passAdbCheck(wired: Boolean) {
            super.passAdbCheck(wired)
            println("passAdbCheck")
            MainWindow(wired)
        }
    }

    // 启动
    Splash(controlListener)
        //*/
}

interface ControlListener {
    fun passFileCheck() {

    }

    fun passAdbCheck(wired: Boolean) {

    }

    fun onConfirmConnection(wired: Boolean) {

    }
}


/* Test Program Fun */
@Suppress("unused")
fun test() {
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
}