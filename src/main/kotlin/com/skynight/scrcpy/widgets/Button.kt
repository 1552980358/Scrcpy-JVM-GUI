package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.ControlCenter
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JButton

class Button: JButton{
    constructor(text: String, width: Int, height: Int): this(text) {
        setSize(width, height)
    }
    constructor(text: String, x: Int, y: Int, width: Int, height: Int): this(text) {
        setBounds(x,y,width, height)
    }
    constructor(text: String): this() {
        setText(text)
    }
    constructor(): super() {
        //background = ControlCenter.getControlCenter.getBGColor()
        foreground = ControlCenter.getControlCenter.getFGColor()
        isFocusPainted = false
    }
}