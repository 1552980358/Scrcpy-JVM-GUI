package com.skynight.scrcpy

import com.skynight.scrcpy.Base.ControlCenter

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {

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
                ADBWiredConnection()
            } else {
                ADBWirelessConnection()
            }
        }

        override fun onConfirmConnection() {
            println("onConfirmConnection")
            TestConnection()
        }

        override fun passAdbCheck() {
            super.passAdbCheck()
            println("passAdbCheck")
            MainWindow()
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


}