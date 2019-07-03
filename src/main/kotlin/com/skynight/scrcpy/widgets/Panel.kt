package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.ControlCenter
import java.awt.Color
import java.awt.LayoutManager
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.border.Border
import javax.swing.border.TitledBorder

class Panel : JPanel {

    constructor(x: Int, y: Int, width: Int, height: Int, layout: LayoutManager?) : this(x, y, width, height) {
        setLayout(layout)
    }

    constructor(x: Int, y: Int, width: Int, height: Int) : this() {
        setBounds(x, y, width, height)
    }

    constructor(width: Int, height: Int, layout: LayoutManager?) : this(width, height) {
        setLayout(layout)
    }

    constructor(width: Int, height: Int) : this() {
        setSize(width, height)
    }

    constructor() : super() {
        background = ControlCenter.getControlCenter.getBGColor()
    }

    fun setUpWithTitledBorder(borderTitle: String) {
        border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ControlCenter.getControlCenter.getFGColor()),
            borderTitle,
            TitledBorder.CENTER,
            TitledBorder.CENTER,
            null,
            ControlCenter.getControlCenter.getFGColor()
        )
    }
}