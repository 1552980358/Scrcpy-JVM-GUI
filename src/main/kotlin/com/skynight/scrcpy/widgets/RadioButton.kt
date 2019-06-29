package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.ControlCenter
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.ImageIcon
import javax.swing.JRadioButton

class RadioButton: JRadioButton {
    lateinit var deviceId: String
    constructor(deviceModel: String, deviceId: String, checked: Boolean): this(deviceModel,deviceId) {
        isSelected = checked
    }
    constructor(deviceModel: String, deviceId: String): this("$deviceModel ($deviceId)") {
        this.deviceId = deviceId
    }

    constructor(title: String, checked: Boolean): this(title) {
        isSelected = checked
    }
    constructor(title: String): this() {
        text = title
    }

    constructor(): super() {
        icon = ImageIcon("icons/RadioButton_UnSelected.png")
        selectedIcon = ImageIcon("icons/RadioButton_Selected.png")
        background = ControlCenter.instance.getBGColor()
        foreground = ControlCenter.instance.getFGColor()
        isFocusPainted = false
    }
}