package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.ControlCenter
import java.awt.LayoutManager
import javax.swing.JLabel

class Label : JLabel {

    constructor(text: String, x: Int, y:Int, width: Int, height: Int, layout: LayoutManager?):
            this(text, x, y, width, height) {
        setLayout(layout)
    }
    constructor(text: String, x: Int, y:Int, width: Int, height: Int): this(text) {
        setBounds(x, y, width, height)
    }

    constructor(text: String, width: Int, height: Int, layout: LayoutManager?): this(text, width, height) {
        setLayout(layout)
    }
    constructor(text: String, width: Int, height: Int): this(text) {
        setSize(width, height)
    }

    constructor(text: String, horizontalAlignment: Int, x: Int, y:Int, width: Int, height: Int, layout: LayoutManager?):
            this(text, horizontalAlignment, x, y, width, height) {
        setLayout(layout)
    }
    constructor(text: String, horizontalAlignment: Int, x: Int, y:Int, width: Int, height: Int): this(text, horizontalAlignment) {
        setBounds(x, y, width, height)
    }

    constructor(text: String, horizontalAlignment: Int, width: Int, height: Int, layout: LayoutManager?): this(text, horizontalAlignment, width, height) {
        setLayout(layout)
    }
    constructor(text: String, horizontalAlignment: Int, width: Int, height: Int): this(text, horizontalAlignment) {
        setSize(width, height)
    }

    constructor(text: String, horizontalAlignment: Int): this(text) {
        setHorizontalAlignment(horizontalAlignment)
    }

    constructor(text: String): this() {
        setText(text)
    }

    constructor(): super() {
        foreground = ControlCenter.instance.getFGColor()
    }
}