package com.skynight.scrcpy.Base

import com.skynight.scrcpy.ControlListener


class ControlCenter private constructor() {

    @Volatile
    @set:Synchronized
    lateinit var controlListener: ControlListener

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
}
