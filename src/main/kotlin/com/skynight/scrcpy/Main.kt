package com.skynight.scrcpy

import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.services.TrayService
import com.skynight.scrcpy.windows.MainWindow
import com.skynight.scrcpy.windows.LogOutputWindow
import com.skynight.scrcpy.windows.SelectLanguageWindow
import com.skynight.scrcpy.windows.SplashWindow
import com.skynight.scrcpy.windows.SelectConnectionWindow
import com.skynight.scrcpy.windows.ADBWiredWindow
import com.skynight.scrcpy.windows.ADBWirelessWindow
import com.skynight.scrcpy.windows.TestConnectionWindow

fun main(@Suppress("UnusedMainParameter") args: Array<String>) {
    //SelectLanguageWindow()

    /* Unit Test */
    //LogOutputWindow.getInstance()
    //SplashWindow()
    //ADBWirelessWindow()
    //ADBWiredWindow()
    //TestConnectionWindow()
    //MainWindow()
    //SelectDeviceWindow()

    //*
    ControlCenter.getControlCenter.setControlListener(object : ControlListener {

        override fun passFileCheck() {
            super.passFileCheck()
            LogOutputWindow.takeLog("Launch Procee: PassFileCheck").newLine()
            SelectConnectionWindow()
        }

        override fun checkUserSave(splash: Boolean) {
            super.checkUserSave(splash)
            LogOutputWindow.takeLog("Launch Procee: CheckUserSave  $splash").newLine()
            TrayService.getTrayService.show()
            if (splash) {
                SplashWindow()
            } else {
                SelectLanguageWindow()
            }
        }

        override fun onHandleConnectionMethod() {
            super.onHandleConnectionMethod()
            if (ControlCenter.getControlCenter.isWiredMethod) {
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