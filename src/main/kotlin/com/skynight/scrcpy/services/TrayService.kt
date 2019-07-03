package com.skynight.scrcpy.services

import com.skynight.scrcpy.base.LoadLanguage
import com.skynight.scrcpy.base.killAllScrcpyProc
import com.skynight.scrcpy.windows.MainWindow
import java.awt.Image
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.PopupMenu
import java.awt.MenuItem
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFrame

class TrayService(image: Image) : TrayIcon(image) {
    companion object {
        val getTrayService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            TrayService(Toolkit.getDefaultToolkit().createImage("icons/icon.jpg"))
        }

        fun show() {
            if (SystemTray.getSystemTray().trayIcons.contains(getTrayService)) {
                return
            }
            SystemTray.getSystemTray().add(getTrayService)
        }

        fun hide(){
            if (!SystemTray.getSystemTray().trayIcons.contains(getTrayService)) {
                return
            }
            SystemTray.getSystemTray().remove(getTrayService)
        }

        fun sendNotification(title: CharSequence, content: CharSequence, type: MessageType): TrayService {
            return getTrayService.also {
                it.displayMessage(title.toString(), content.toString(), type)
            }
        }
    }

    init {
        isImageAutoSize = true
        toolTip = "Scrcpy - JVM GUI"
        popupMenu = PopupMenu()
        popupMenu.add(MenuItem(LoadLanguage.getLoadLanguage.getTrayStrings().get("exit").asString).also {
            it.addActionListener {
                Thread {
                    killAllScrcpyProc()
                    System.exit(0)
                }.start()
            }
        })
        popupMenu.add(MenuItem(LoadLanguage.getLoadLanguage.getTrayStrings().get("closeScrcpy").asString).also {
            it.addActionListener {
                Thread {
                    killAllScrcpyProc()
                }.start()
            }
        })

        addMouseListener(object : MouseListener {
            override fun mouseReleased(e: MouseEvent?) {
            }

            override fun mouseEntered(e: MouseEvent?) {
            }

            override fun mouseClicked(e: MouseEvent?) {
                Thread {
                    if (MainWindow.isCreated() && e!!.button == MouseEvent.BUTTON1) {
                        MainWindow.instance.also {
                            it.extendedState = JFrame.NORMAL
                            it.toFront()
                        }
                    }
                }.start()
            }

            override fun mouseExited(e: MouseEvent?) {
             }

            override fun mousePressed(e: MouseEvent?) {
            }
        })

    }

    fun show() {
        Companion.show()
    }

    fun hide(){
        Companion.hide()
    }

    fun sendNotification(title: CharSequence, content: CharSequence, type: MessageType = MessageType.INFO): TrayService {
        return Companion.sendNotification(title, content, type)
    }
}