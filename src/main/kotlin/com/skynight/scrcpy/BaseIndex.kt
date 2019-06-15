package com.skynight.scrcpy

import java.io.File

class BaseIndex {
    companion object {
        const val WidgetWithTextHeight = 26

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
        val ADBWirelessStepsText = arrayListOf(
            "1.请于开发者选项中打开ADB调试, 然后使用USB连接计算机\n" +
                    "开发者选项打开方式:\n" +
                    "设置 → 关于{设备/手机} →\n" +
                    "\n" +
                    "MIUI\n" +
                    " - 多次点击\"MIUI版本\" → 返回 → 更多设置 → 开发者选项\n" +
                    "原生/类原生 Pie\n" +
                    " - 多次点击版本号 → 系统 → 高级 → 开发者选项\n",
            "2.请连接与计算机相同的WiFi网络\n",
            "3.设置端口中...\n",
            "4.正在自动获取WiFi/Wlan的IP地址...\n",
            "5.请断开USB连接",
            "6.点击下方完成"
        )
        val ADBWirelessStepsBtn = arrayListOf(
            "打开WiFi/Wlan设置",
            "设置端口",
            "获取WiFi/Wlan的IP地址",
            "下一步",
            "下一步",
            "完成"
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
    }
}