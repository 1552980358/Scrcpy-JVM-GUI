package com.skynight.scrcpy.widgets

import com.skynight.scrcpy.base.ControlCenter
import javax.swing.JLabel

class Label : JLabel {

    companion object {
        const val LABEL_CENTER = 0
        const val LABEL_TOP = 1
        const val LABEL_LEFT = 2
        const val LABEL_BOTTOM = 3
        const val LABEL_RIGHT = 4
    }

    /*
     * @param String
     * @param Int
     * @param Int @param Int
     * -> fun(String, Int, Int, Int)
     */
    @Suppress("unused")
    constructor(text: String, horizontalAlignment: Int, width: Int, height: Int) : this(text, width, height) {
        setHorizontalAlignment(horizontalAlignment)
    }

    /*
    * @param String
    * @param Int
    * @param Int @param Int
    * -> fun(String)
    */
    constructor(text: String, width: Int, height: Int) : this(text) {
        setSize(width, height)
    }

    /*
     * @param String
     * @param Int
     * @param Int @param Int
     * @param Int @param Int
     * -> fun(String, Int, Int, Int, Int)
     */
    constructor(text: String, horizontalAlignment: Int, x: Int, y: Int, width: Int, height: Int) :
            this(text, x, y, width, height) {
        setHorizontalAlignment(horizontalAlignment)
    }

    /*
     * @param String
     * @param Int @param Int
     * @param Int @param Int
     * -> fun(String)
     */
    constructor(text: String, x: Int, y: Int, width: Int, height: Int) : this(text) {
        setBounds(x, y, width, height)
    }

    /*
     * @param String
     * @param Int
     * -> fun(String)
     */
    constructor(text: String, horizontalAlignment: Int) : this(text) {
        setHorizontalAlignment(horizontalAlignment)
    }

    /*
     * @param String
     * -> fun()
     */
    constructor(text: String) : this() {
        setText(text)
    }

    /* fun Init */
    constructor() : super() {
        foreground = ControlCenter.getControlCenter.getFGColor()
    }
}