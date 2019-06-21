package com.skynight.scrcpy.base

import com.skynight.scrcpy.LogOutputWindow
import com.skynight.scrcpy.base.BaseIndex.Companion.PackageFileList
import java.awt.Color
import java.io.File
import java.util.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.collections.ArrayList

fun exitButton(jFrame: JFrame, jPanel: JPanel) {
    jFrame.title = "出现错误!"
    val jButton = JButton("确定")
    jButton.isVisible = false
    jButton.setBounds(10, 30, 260, 40)
    jButton.background = Color.BLACK
    jButton.foreground = Color.WHITE
    jPanel.add(jButton)
    jButton.isVisible = true
    jButton.addActionListener {
        ControlCenter.instance.getControlListener().passFileCheck()
        jFrame.dispose()
    }
}

fun runAdb(commands: String): Boolean {
    return runAdb(if (commands.contains(" ")) {
        val arrayList = ArrayList(Arrays.asList(
            *commands
                .split(" ".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()))
        val tmp = mutableListOf("adb")
        for (i in arrayList) {
            tmp.add(i)
        }
        tmp.toTypedArray()
    } else {
        arrayOf("adb", commands)
    })
}

fun runAdb(commands: Array<String>): Boolean {
    try {
        // 已有ADB
        Runtime.getRuntime()
            .exec(commands)
            .inputStream.bufferedReader().readLines()
    } catch (e: Exception) {
        LogOutputWindow.takeLog(e)
        try {
            // 封装ADB
            Runtime.getRuntime()
                .exec(arrayOf(System.getProperty("user.dir") + File.separator + PackageFileList[0], "devices"))
                .inputStream.bufferedReader().readLines()
        } catch (e: Exception) {
            LogOutputWindow.takeLog(e)
            return false
        }
    }
    return true
}

fun runAdbGetText(commands: String): String {
    val text = StringBuilder()
    for (i in runAdbGetList(commands)) {
        text.append(i + "\n")
    }
    return text.toString()
}
/*
fun runAdbGetList(commands: String): List<String> {
    return try {
        // 已有ADB
        val p = Runtime.getRuntime()
            .exec("adb $commands")
        p.waitFor()
        p.inputStream.bufferedReader().readLines()
    } catch (e: Exception) {
        e.printStackTrace()
        try {
            // 封装ADB
            val p = Runtime.getRuntime()
                .exec(System.getProperty("user.dir") + File.separator + PackageFileList[0] + commands)
            p.waitFor()
            p.inputStream.bufferedReader().readLines()
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }
}
*/

fun runAdbGetList(command: String): List<String> {
    val array = if (command.contains(" ")) {
        val arrayList = ArrayList(Arrays.asList(
            *command
                .split(" ".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()))

        val tmp = mutableListOf("adb")
        for (i in arrayList) {
            tmp.add(i)
        }
        tmp.toList().toTypedArray()
    } else {
        arrayOf("adb", command)
    }

    return runAdbGetList(array)
}


fun runAdbGetList(command: Array<String>): List<String> {
    LogOutputWindow.takeLog("Command: " + command.toList().toString())
    return try {
        // 已有ADB
        val p = Runtime.getRuntime()
            .exec(command)
        p.waitFor()
        p.inputStream.bufferedReader().readLines()
    } catch (e: Exception) {
        LogOutputWindow.takeLog(e)
        try {
            command[0] = System.getProperty("user.dir") + File.separator + PackageFileList[0]
            // 封装ADB
            val p = Runtime.getRuntime()
                .exec(command)
            p.waitFor()
            p.inputStream.bufferedReader().readLines()
        } catch (e: Exception) {
            LogOutputWindow.takeLog(e)
            return listOf()
        }
    }
}

fun checkAdbConnect(): Boolean {
    val result = runAdbGetList("devices")

    if (result[1].isEmpty()) {
        LogOutputWindow.takeLog("No Device")
        return false
    }

    for ((j, i) in result.withIndex()) {
        if (i.contains("device") && j != 0) {
            LogOutputWindow.takeLog("Device Connected")
            return true
        }
        if (i.contains("offline")) {
            LogOutputWindow.takeLog("Device Offline")
            return false
        }
        if (i.contains("unauthroized")) {
            LogOutputWindow.takeLog("Device Unauthorized")
            return false
        }
    }
    return false
}