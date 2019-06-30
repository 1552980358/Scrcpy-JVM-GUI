package com.skynight.scrcpy.base

import com.google.gson.JsonParser
import com.skynight.scrcpy.ControlListener
import com.skynight.scrcpy.windows.LogOutputWindow
import com.skynight.scrcpy.base.BaseIndex.Companion.DataSave
import com.skynight.scrcpy.base.BaseIndex.Companion.SaveColor
import com.skynight.scrcpy.base.BaseIndex.Companion.SaveConsoleSetting
import java.awt.Color
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

    @Volatile
    private var FGColor = Color(0, 0, 0)
    @Volatile
    private var BGColor = Color(255, 255, 255)

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ControlCenter()
        }
    }

    init {
        val t1 = Thread {
            val file = File(DataSave + File.separator + "CustomSettings")
            try {
                LogOutputWindow.instance
                if (file.exists()) {
                    try {
                        val jsonFile = file.inputStream().bufferedReader().readText()
                        LogOutputWindow.takeLog("Read Custom Settings\n$jsonFile")
                        val json = JsonParser().parse(jsonFile).asJsonObject

                        logOutputWindow = json.get("LogOutputWindow").asBoolean
                        consoleless = json.get("Consoleless").asBoolean
                        tips = json.get("Tips").asBoolean

                    } catch (e: Exception) {
                        LogOutputWindow.takeLog(e)
                        changeConsoleSettings()
                    }
                } else {
                    changeConsoleSettings()
                }
                LogOutputWindow.instance.isVisible = logOutputWindow
                LogOutputWindow.takeLog("LogOutputWindow: $logOutputWindow")
                    .takeLog("Consoleless: $consoleless")
                    .takeLog("Tips: $tips")
                    .newLine()
            } catch (e: Exception) {
                e.printStackTrace()
                LogOutputWindow.takeLog(e)
            }
        }
        t1.start()

        val t2 = Thread {
            try {
                val file = File(DataSave + File.separator + "ColorSettings")
                if (file.exists()) {
                    try {
                        val jsonFile = file.inputStream().bufferedReader().readText()
                        LogOutputWindow.takeLog("Read Color Settings\n$jsonFile")
                        val json = JsonParser().parse(jsonFile).asJsonObject
                        val bg = json.get("BGColor").asJsonObject
                        BGColor = Color(bg.get("R").asInt, bg.get("G").asInt, bg.get("B").asInt)

                        val fg = json.get("FGColor").asJsonObject
                        FGColor = Color(fg.get("R").asInt, fg.get("G").asInt, fg.get("B").asInt)


                    } catch (e: Exception) {
                        LogOutputWindow.takeLog(e)
                    }
                } else {

                }
            } catch (e: Exception) {
                e.printStackTrace()
                LogOutputWindow.takeLog(e)
            }
        }
        t2.start()

        // WaitFor complete of two threads
        var timer = 0
        while (t1.isAlive || t2.isAlive) {
            try {
                Thread.sleep(1)
            } catch (e: Exception) {
                //e.printStackTrace()
            }
            timer++
        }
        LogOutputWindow.takeLog("Initial Load Custom Settings Time spend: ${timer}ms").newLine()
    }

    fun setLogOutputWindow(boolean: Boolean) {
        this.logOutputWindow = boolean
        changeConsoleSettings()
    }

    fun getLogOutputWindow(): Boolean {
        return this.logOutputWindow
    }

    fun setConsoleless(boolean: Boolean) {
        this.consoleless = boolean
        changeConsoleSettings()
    }
    fun getConsoleless(): Boolean {
        return this.consoleless
    }

    fun setTips(boolean: Boolean) {
        this.tips = boolean
        changeConsoleSettings()
    }
    fun getTips(): Boolean {
        return this.tips
    }

    fun getBGColor(): Color {
        return BGColor
    }
    fun setBGColor(color: Color?) {
        color?.let {
            if (color.rgb == BGColor.rgb) {
                return
            }
            this.BGColor = color
            changeColorSettings()
        }
    }

    fun getFGColor(): Color {
        return FGColor
    }
    fun setFGColor(color: Color?) {
        color?.let {
            if (color.rgb == FGColor.rgb) {
                return
            }
            this.FGColor = color
            changeColorSettings()
        }
    }

    private fun changeConsoleSettings() {
        val file = File(DataSave + File.separator + "CustomSettings")
        try {
            if (!file.exists()){
                file.createNewFile()
            }
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

    private fun changeColorSettings() {
        val file = File(DataSave + File.separator + "ColorSettings")
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileWriter = FileWriter(file, false)
            /* Background Color RGB,  Foreground Color RGB*/
            val s = String.format(SaveColor,
                BGColor.red, BGColor.green, BGColor.blue,
                FGColor.red, FGColor.green, FGColor.blue
            )
            LogOutputWindow.takeLog("$file changed as:\n$s")
            fileWriter.write(s)
            fileWriter.flush()
            fileWriter.close()
        } catch (e: Exception) {
            LogOutputWindow.takeLog(e)
        }
    }
}
