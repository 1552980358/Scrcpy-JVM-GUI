package com.skynight.scrcpy.Base

import java.io.File

class BaseIndex {
    companion object {
        const val WidgetWithTextHeight = 27

        val PackageFileList = arrayListOf(
            "package${File.separator}adb.exe",
            "package${File.separator}AdbWinApi.dll",
            "package${File.separator}AdbWinUsbApi.dll",
            "package${File.separator}avcodec-58.dll",
            "package${File.separator}avformat-58.dll",
            "package${File.separator}avutil-56.dll",
            "package${File.separator}scrcpy.exe",
            "package${File.separator}scrcpy-server.jar",
            "package${File.separator}SDL2.dll",
            "package${File.separator}swresample-3.dll",
            "package${File.separator}swscale-5.dll"
        )
        val ScrcpyArgsList = arrayListOf(
            "-f",
            "-n",
            "-T",
            "-t",
            "--render-expired-frames"
        )
        @Suppress("BooleanLiteralArgument", "SpellCheckingInspection")
        val ScrcpyGetOrder = arrayListOf(false, true, false, false, false)

        val BitRateList = mapOf(
            1 to "30",
            2 to "20",
            3 to "4"
        )

        @JvmField val ControlKeyFunctionList = arrayListOf(
            "全屏模式",
            "重置窗口大小至1:1(完美像素)",
            "重置大小消除黑色边框",
            "主页 HOME",
            "返回 BACK",
            "应用管理 APP_SWITCH",
            "菜单 MENU",
            "音量+ VOLUME_UP",
            "音量- VOLUME_DOWN",
            "电源键 POWER",
            "关闭屏幕并保持投屏",
            "展开通知面板",
            "打开崩溃通知面板",
            "设备剪贴板复制到计算机",
            "粘贴计算机剪贴板到设备",
            "将计算机剪贴板复制到设备",
            "打开/关闭FPS计算(于控制台)"
        )
        @JvmField val ControlKeyList = mapOf(
            ControlKeyFunctionList[0] to "Ctrl + f",
            ControlKeyFunctionList[1] to "Ctrl + g",
            ControlKeyFunctionList[2] to "Ctrl + x",
            ControlKeyFunctionList[3] to "Ctrl + h",
            ControlKeyFunctionList[4] to "Ctrl + b",
            ControlKeyFunctionList[5] to "Ctrl + s",
            ControlKeyFunctionList[6] to "Ctrl + m",
            ControlKeyFunctionList[7] to "Ctrl + ↑",
            ControlKeyFunctionList[8] to "Ctrl + ↓",
            ControlKeyFunctionList[9] to "Ctrl + p",
            ControlKeyFunctionList[10] to "Ctrl + o",
            ControlKeyFunctionList[11] to "Ctrl + n",
            ControlKeyFunctionList[12] to "Ctrl + Shift + n",
            ControlKeyFunctionList[13] to "Ctrl + c",
            ControlKeyFunctionList[14] to "Ctrl + v",
            ControlKeyFunctionList[15] to "Ctrl + Shft + v",
            ControlKeyFunctionList[16] to "Ctrl + i"
        )
    }
}