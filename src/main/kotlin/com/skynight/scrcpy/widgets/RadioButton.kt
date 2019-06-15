package com.skynight.scrcpy.widgets

import javax.swing.ImageIcon
import javax.swing.JRadioButton

class RadioButton(title: String): JRadioButton(title) {
    init {
        icon = ImageIcon("icons/RadioButton_UnSelected.png")
        selectedIcon = ImageIcon("icons/RadioButton_Selected.png")
    }
}