package com.skynight.scrcpy.widgets

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
    constructor(text: String): super() {
        setText(text)
        background = Color.WHITE
        border = BorderFactory.createLineBorder(Color.BLACK)
        isFocusPainted = false
    }
}