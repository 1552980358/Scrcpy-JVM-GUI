package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.BaseIndex
import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.JCheckBox

class CheckBox: JCheckBox {
    private var index: Int = 0
    lateinit var deviceId: String
    constructor(deviceModel: String, deviceId: String): this("$deviceModel ($deviceId)") {
        this.deviceId = deviceId
    }

    constructor(title: String, selection: Boolean, index: Int): this(title) {
        isSelected = selection
        this.index = index
    }
    constructor(title: String, selection: Boolean): this(title) {
        isSelected = selection
    }
    constructor(title: String) : this() {
        text = title
    }

    constructor(): super() {
        background = Color.WHITE
        icon = ImageIcon("icons/CheckBox_UnSelected.png")
        selectedIcon = ImageIcon("icons/CheckBox_Selected.png")
        isFocusPainted = false
    }

    fun getArg(): String {
        return if (BaseIndex.ScrcpyGetOrder[index] != isSelected) {
            BaseIndex.ScrcpyArgsList[index]
        } else {
            ""
        }
    }
}