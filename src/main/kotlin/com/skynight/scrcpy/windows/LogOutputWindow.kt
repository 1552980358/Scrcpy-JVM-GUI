package com.skynight.scrcpy.windows

import java.awt.Color
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class LogOutputWindow: JFrame("Logging") {
    lateinit var listener: LogWindowOperationListener

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            LogOutputWindow()
        }
        private val jTextArea = JTextArea()

        fun takeLog(log: Any): LogOutputWindow {
            return try {
                takeLog(log.toString())
            } catch (e: Exception) {
                takeLog(e)
                instance
            }
        }
        fun takeLog(log: Exception): LogOutputWindow {
            return takeLog(log.toString())
        }
        fun takeLog(log: CharSequence): LogOutputWindow {
            jTextArea.append("$log\n")
            return instance
        }
        fun newLine(): LogOutputWindow {
            return takeLog("")
        }
    }

    init {
        setSize(600, 350)
        isResizable = false
        setLocation(0, 0)
        addWindowListener(object : WindowListener {
            override fun windowDeiconified(e: WindowEvent?) {

            }

            override fun windowClosing(e: WindowEvent?) {
            }

            override fun windowClosed(e: WindowEvent?) {
                isVisible = false
            }

            override fun windowActivated(e: WindowEvent?) {
            }

            override fun windowDeactivated(e: WindowEvent?) {
            }

            override fun windowOpened(e: WindowEvent?) {
            }

            override fun windowIconified(e: WindowEvent?) {
            }

        })

        val mainPanel = JPanel()
        mainPanel.setSize(0, 0)
        mainPanel.layout = null
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
    fun takeLog(log: Any): LogOutputWindow {
        return Companion.takeLog(log)
    }
    fun takeLog(log: Exception): LogOutputWindow? {
        return Companion.takeLog(log)
    }
    fun takeLog(log: CharSequence): LogOutputWindow {
        return Companion.takeLog(log)
    }
    fun newLine(): LogOutputWindow {
        return Companion.newLine()
    }

    override fun setVisible(b: Boolean) {
        super.setVisible(b)
        if (::listener.isInitialized) {
            listener.onStateChange(b)
        }
    }
}

interface LogWindowOperationListener {
    fun onStateChange(isVisible: Boolean) {}
}