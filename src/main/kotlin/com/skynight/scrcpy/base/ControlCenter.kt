package com.skynight.scrcpy.base

import com.google.gson.JsonParser
import com.skynight.scrcpy.ControlListener
import com.skynight.scrcpy.LogOutputWindow
import com.skynight.scrcpy.base.BaseIndex.Companion.DataSave
import com.skynight.scrcpy.base.BaseIndex.Companion.SaveConsoleSetting
import java.io.File
import java.io.FileWriter

class ControlCenter {
    @Volatile
    private lateinit var controlListener: ControlListener

    fun setControlListener(controlListener: ControlListener) {
        this.controlListener = controlListener
        LogOutputWindow.takeLog("StartLoading")
        LoadLanguage.instance
    }

    fun getControlListener(): ControlListener {
        return this.controlListener
    }

    @Volatile
    @set:Synchronized
    var isWiredMethod = true

    @Volatile
    private var logOutputWindow = true
    @Volatile
    private var consoleless = false
    @Volatile
    private var tips = true

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ControlCenter()
        }
    }

    init {
        val file = File(DataSave + File.separator + "CustomSettings")
        try {
            LogOutputWindow.instance
            if (file.exists()) {
                val jsonFile = file.inputStream().bufferedReader().readText()
                LogOutputWindow.takeLog("Read Custom Setting\n$jsonFile")
                val json = JsonParser().parse(jsonFile).asJsonObject
                logOutputWindow = json.get("LogOutputWindow").asBoolean
                consoleless = json.get("Consoleless").asBoolean
                tips = json.get("Tips").asBoolean
                println(logOutputWindow.toString() + consoleless.toString() + tips.toString())
            } else {
                changeConsoleSetting()
            }
            LogOutputWindow.instance.isVisible = logOutputWindow
            LogOutputWindow.takeLog("LogOutputWindow: $logOutputWindow")
                .takeLog("Consoleless: $consoleless")
                .newLine()
        } catch (e: Exception) {
            e.printStackTrace()
            LogOutputWindow.takeLog(e)
        }
    }

    fun setLogOutputWindow(boolean: Boolean) {
        this.logOutputWindow = boolean
        changeConsoleSetting()
    }

    fun getLogOutputWindow(): Boolean {
        return this.logOutputWindow
    }

    fun setConsoleless(boolean: Boolean) {
        this.consoleless = boolean
        changeConsoleSetting()
    }
    fun getConsoleless(): Boolean {
        return this.consoleless
    }

    fun setTips(boolean: Boolean) {
        this.tips = boolean
        changeConsoleSetting()
    }
    fun getTips(): Boolean {
        return this.tips
    }

    private fun changeConsoleSetting() {
        val file = File(DataSave + File.separator + "CustomSettings")
        try {
            if (!file.exists())
                file.createNewFile()
            val fileWriter = FileWriter(file, false)
            val s = String.format(SaveConsoleSetting, logOutputWindow, consoleless, tips)
            LogOutputWindow.takeLog("$file changed as:\n$s")
            fileWriter.write(s)
            fileWriter.flush()
            fileWriter.close()
            LogOutputWindow.instance.isVisible = logOutputWindow
        } catch (e: Exception) {
            LogOutputWindow.takeLog(e)
        }
    }
}
