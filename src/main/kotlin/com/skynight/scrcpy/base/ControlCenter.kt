package com.skynight.scrcpy.base

import com.google.gson.JsonParser
import com.skynight.scrcpy.ControlListener
import com.skynight.scrcpy.LogOutputWindow
import com.skynight.scrcpy.base.BaseIndex.Companion.DataSave
import com.skynight.scrcpy.base.BaseIndex.Companion.SaveConsoleSetting
import java.io.File
import java.io.FileWriter
import java.lang.ref.SoftReference
import kotlin.math.log

class ControlCenter {
    @Volatile
    private lateinit var controlListener: ControlListener

    fun setControlListener(controlListener: ControlListener) {
        this.controlListener = controlListener
        LogOutputWindow.takeLog("startLoading")
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

    companion object {
        /*
        private var instance: ControlCenter? = null
        @Synchronized
        fun getInstance(): ControlCenter {
        if (instance == null) {
            instance = ControlCenter()
        }
            return instance as ControlCenter
        }*/
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ControlCenter()
        }
    }

    init {
        val file = File(DataSave + File.separator + "ConsoleSetting")
        try {
            if (file.exists()) {
                val json = JsonParser().parse(file.inputStream().bufferedReader().readText()).asJsonObject
                logOutputWindow = json.get("LogOutputWindow").asBoolean
                consoleless = json.get("Consoleless").asBoolean
            } else {
                changeConsoleSetting()
            }
            LogOutputWindow.instance.isVisible = logOutputWindow
            LogOutputWindow.takeLog("logOutputWindow: $logOutputWindow")
                .takeLog("consoleless: $consoleless")
        } catch (e: Exception) {
            LogOutputWindow.instance.takeLog(e)
        }
    }

    fun setLogOutputWindow(boolean: Boolean) {
        this.logOutputWindow = boolean
        changeConsoleSetting()
    }

    fun getLogOutputWindow(): Boolean {
        return this.logOutputWindow
    }

    fun getConsoleless(): Boolean {
        return this.consoleless
    }

    fun setConsoleless(boolean: Boolean) {
        this.consoleless = boolean
        changeConsoleSetting()
    }

    private fun changeConsoleSetting() {
        val file = File(DataSave + File.separator + "ConsoleSetting")
        try {
            if (!file.exists())
                file.createNewFile()
            val fileWriter = FileWriter(file, false)
            val s = String.format(SaveConsoleSetting, logOutputWindow, consoleless)
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
