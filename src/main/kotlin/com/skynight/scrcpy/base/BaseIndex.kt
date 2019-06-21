package com.skynight.scrcpy.base

import java.io.File
import java.io.File.separator

class BaseIndex {
    companion object {
        const val WidgetWithTextHeight = 26
        const val PanelMarginRight = 16

        const val DefaultLocale = "zh-rCN"
        val LanguageDir = System.getProperty("user.dir") + separator + "language"
        val DataSave = System.getProperty("user.dir") + separator + "save"
        const val SaveConsoleSetting = "{\n\t\"LogOutputWindow\": %s,\n\t\"Consoleless\": %s\n}\n"
        const val SaveLocale = "{\n\t\"language\": \"%s\", \n\t\"region\": \"%s\"\n}"

        val PackageFileList = arrayListOf(
            "package${File.separator}adb.exe",
            "package${File.separator}AdbWinApi.dll",
            "package${File.separator}AdbWinUsbApi.dll",
            "package${File.separator}avcodec-58.dll",
            "package${File.separator}avformat-58.dll",
            "package${File.separator}avutil-56.dll",
            "package${File.separator}scrcpy.exe",
            "package${File.separator}scrcpy-noconsole.exe",
            "package${File.separator}scrcpy-server.jar",
            "package${File.separator}SDL2.dll",
            "package${File.separator}swresample-3.dll",
            "package${File.separator}swscale-5.dll"
        )

        val PackageFilesMD5 = mapOf(
            PackageFileList[0] to "96c1a1f9ece6eee131f57117717d5990",
            PackageFileList[1] to "ed5a809dc0024d83cbab4fb9933d598d",
            PackageFileList[2] to "0e24119daf1909e398fa1850b6112077",
            PackageFileList[3] to "90e5bedbf66b0beb6cf291f57d6dade3",
            PackageFileList[4] to "34f13050e6ed8e1a5211b7edc85e8501",
            PackageFileList[5] to "8634e07c4df856cafa40a0af7ad61d07",
            PackageFileList[6] to "6101e65c165479a0caa0e87ae304df86",
            PackageFileList[7] to "73188f4f4c9b518e45f050a0f3e95531",
            PackageFileList[8] to "1ba41bc8fb734d3e31e2eef1f3077314",
            PackageFileList[9] to "6f0469c91c605754eb64163b5f9014ad",
            PackageFileList[10] to "addca5fa064b7b205199f8769f28e503",
            PackageFileList[11] to "d54df450246c705ceb3286dd95dce4b2"
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
            "打开/关闭FPS计算"
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