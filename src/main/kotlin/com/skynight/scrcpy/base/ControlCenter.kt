package com.skynight.scrcpy.base

import com.skynight.scrcpy.ControlListener
import com.skynight.scrcpy.LogOutputWindow

class ControlCenter private constructor() {
    private val loadLanguage = LoadLanguage()
    fun getLoadLanguage(): LoadLanguage {
        return loadLanguage
    }

    @Volatile
    private lateinit var controlListener: ControlListener
    fun setControlListener(controlListener: ControlListener) {
        this.controlListener = controlListener
        loadLanguage.start()
    }
    fun getControlListener(): ControlListener {
        return this.controlListener
    }

    @Volatile
    @set:Synchronized
    var isWiredMethod = true

    companion object {
        private var instance: ControlCenter? = null
        @Synchronized
        fun getInstance(): ControlCenter {
            if (instance == null) {
                instance = ControlCenter()
            }
            return instance as ControlCenter
        }
    }
    init {
        LogOutputWindow.getInstance()
    }
}
