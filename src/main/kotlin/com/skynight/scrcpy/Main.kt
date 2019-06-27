package com.skynight.scrcpy

import com.skynight.scrcpy.base.ControlCenter

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
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