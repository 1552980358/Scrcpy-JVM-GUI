package com.skynight.scrcpy.Base

import com.skynight.scrcpy.Base.BaseIndex.Companion.PackageFileList
import com.sun.jna.StringArray
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
        System.exit(0)
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
    println(commands)
    try {
        // 已有ADB
        Runtime.getRuntime()
            .exec(commands)
            .inputStream.bufferedReader().readLines()
    } catch (e: Exception) {
        e.printStackTrace()
        try {
            // 封装ADB
            Runtime.getRuntime()
                .exec(arrayOf(System.getProperty("user.dir") + File.separator + PackageFileList[0], "devices"))
                .inputStream.bufferedReader().readLines()
        } catch (e: Exception) {
            e.printStackTrace()
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

fun runAdbGetList(commands: String): List<String> {
    //println(commands)
    val array = if (commands.contains(" ")) {
        //println("1")
        val arrayList = ArrayList(Arrays.asList(
            *commands
                .split(" ".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()))

        val tmp = mutableListOf("adb")
        for (i in arrayList) {
            tmp.add(i)
        }
        tmp.toList().toTypedArray()
    } else {
        //println("2")
        arrayOf("adb", commands)
    }

    //println(array)

    return runAdbGetList(array)
}


fun runAdbGetList(commands: Array<String>): List<String> {
    //println(commands)

    return try {
        // 已有ADB
        val p = Runtime.getRuntime()
            .exec(commands)
        p.waitFor()
        p.inputStream.bufferedReader().readLines()
    } catch (e: Exception) {
        e.printStackTrace()
        try {
            commands[0] = System.getProperty("user.dir") + File.separator + PackageFileList[0]
            // 封装ADB
            val p = Runtime.getRuntime()
                .exec(commands)
            p.waitFor()
            p.inputStream.bufferedReader().readLines()
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }
}

fun checkAdbConnect(): Boolean {
    val result = runAdbGetList("devices")

    if (result[1].isEmpty()) {
        println("无设备")
        return false
    }

    for ((j,i) in result.withIndex()) {
        if (i.contains("device") && j != 0) {
            print("最少有一个设备连接")
            return true
        }
        if (i.contains("offline")) {
            print("设备离线")
            return false
        }
    }
    return false
}