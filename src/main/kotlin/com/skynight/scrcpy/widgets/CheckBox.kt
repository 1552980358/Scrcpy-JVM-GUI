package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.BaseIndex
import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.JCheckBox

class CheckBox(title: String, selection: Boolean, private val index: Int): JCheckBox(title) {
    init {
        isSelected = selection
        background = Color.WHITE
        icon = ImageIcon("icons/CheckBox_UnSelected.png")
        selectedIcon = ImageIcon("icons/CheckBox_Selected.png")
    }
    fun getArg(): String {
        return if (BaseIndex.ScrcpyGetOrder[index] != isSelected) {
            BaseIndex.ScrcpyArgsList[index]
        } else {
            ""
        }
    }
}