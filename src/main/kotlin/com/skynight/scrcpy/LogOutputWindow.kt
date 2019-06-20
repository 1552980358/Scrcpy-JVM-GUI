package com.skynight.scrcpy

import com.skynight.scrcpy.widgets.Panel
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class LogOutputWindow: JFrame("Logging") {

    companion object {
        private var instance: LogOutputWindow? = null
        @Synchronized
        fun getInstance(): LogOutputWindow {
            if (instance == null) {
                instance = LogOutputWindow()
            }
            return instance as LogOutputWindow
        }
        private val jTextArea = JTextArea()
        fun takeLog(log: Exception) {
            takeLog(log.toString())
        }
        fun takeLog(log: CharSequence) {
            jTextArea.append("$log\n")
        }
    }

    init {
        setSize(600, 350)
        isResizable = false
        setLocation(0, 0)
        defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
        isVisible = true

        val mainPanel = Panel(0, 0, null)
        add(mainPanel)
        mainPanel.isVisible = true
        val jScrollPane = JScrollPane(jTextArea)
        jScrollPane.setBounds(0, 0, width - 16, height - 20)
        jScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        mainPanel.add(jScrollPane)
        jTextArea.isEditable = false
        jTextArea.lineWrap = true
        jTextArea.background = Color.BLACK
        jTextArea.foreground = Color.WHITE
        jTextArea.setBounds(0, 0, width - 16, height - 20)
        jTextArea.document.addDocumentListener(object : DocumentListener{
            override fun changedUpdate(e: DocumentEvent?) {
                jTextArea.caretPosition = jTextArea.text.length
            }

            override fun insertUpdate(e: DocumentEvent?) {
                jTextArea.caretPosition = jTextArea.text.length
            }

            override fun removeUpdate(e: DocumentEvent?) {
                jTextArea.caretPosition = jTextArea.text.length
            }
        })
    }


}