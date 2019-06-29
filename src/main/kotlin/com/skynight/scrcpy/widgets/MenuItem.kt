package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.ControlCenter
import javax.swing.JMenuItem

class MenuItem(title: String) : JMenuItem(title) {
    init {
        background = ControlCenter.instance.getBGColor()
        foreground = ControlCenter.instance.getFGColor()
    }
}