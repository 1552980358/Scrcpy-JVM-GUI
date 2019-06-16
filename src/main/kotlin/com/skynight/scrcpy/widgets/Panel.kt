package com.skynight.scrcpy.widgets

import java.awt.Color
import java.awt.LayoutManager
import javax.swing.JPanel

class Panel: JPanel {
    companion object {
        const val WidthBorder = 16
    }

    constructor(x: Int, y: Int, width: Int, height: Int): this() {
        setBounds(x, y, width - WidthBorder, height)
    }
    constructor(width: Int, height: Int) : this() {
        setSize(width - WidthBorder, height)
    }
    constructor(x: Int, y: Int, width: Int, height: Int, layout: LayoutManager?): this() {
        setBounds(x, y, width - WidthBorder, height)
        setLayout(layout)
    }
    constructor(width: Int, height: Int, layout: LayoutManager?): this() {
        setSize(width - WidthBorder, height)
        setLayout(layout)
    }
    constructor(): super() {
        background = Color.WHITE
    }
}