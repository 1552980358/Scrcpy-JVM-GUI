package com.skynight.scrcpy

import com.google.gson.JsonObject
import com.skynight.scrcpy.Base.*
import com.skynight.scrcpy.widgets.Button
import java.awt.Color
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.lang.StringBuilder
import javax.swing.*

class ADBWiredWindow : JFrame() {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize

        val jsonObject = DecodeLanguagePack.getInstance().getWindowStrings("ADBWiredWindow")

        title = jsonObject.get("title").asString
        setSize(350, 300)
        isAlwaysOnTop = true
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = true

        addComponentListener(object : ComponentListener {
            override fun componentMoved(e: ComponentEvent?) {
                setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
            }

            override fun componentResized(e: ComponentEvent?) {

            }

            override fun componentHidden(e: ComponentEvent?) {
            }

            override fun componentShown(e: ComponentEvent?) {

            }

        })

        val jPanel = JPanel()
        add(jPanel)
        jPanel.isVisible = false
        jPanel.background = Color.WHITE
        jPanel.layout = null
        jPanel.setSize(350, 300)
        val jTextArea = JTextArea(jsonObject.get("step").asString)
        jPanel.add(jTextArea)
        jTextArea.isEditable = false
        jTextArea.setBounds(5, 0, 350, 184)

        val jButton = Button(jsonObject.get("step_done").asString, 10, 200, 310, 50)
        jButton.addActionListener {
            if (!MainWindow.isCreated()){
                ControlCenter.getInstance().controlListener.onConfirmConnection()
            }
            dispose()
        }
        jPanel.add(jButton)

        jPanel.isVisible = true
    }
}

class ADBWirelessWindow : JFrame() {
    var jsonObject: JsonObject

    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        jsonObject = DecodeLanguagePack.getInstance().getWindowStrings("ADBWirelessWindow")

        title = jsonObject.get("title").asString
        setSize(350, 300)
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = true

        val jPanel = JPanel()
        add(jPanel)
        jPanel.isVisible = false
        jPanel.background = Color.WHITE
        jPanel.layout = null
        jPanel.setSize(350, 300)

        @Suppress("LocalVariableName") val ADBWirelessStepsText = jsonObject.get("steps").asJsonArray
        @Suppress("LocalVariableName") val ADBWirelessStepsBtn = jsonObject.get("steps_btn").asJsonArray

        var page = 0
        val jTextArea = JTextArea(ADBWirelessStepsBtn[0].toString())
        jPanel.add(jTextArea)
        jTextArea.isEditable = false
        jTextArea.setBounds(5, 0, 350, 150)

        // IP 抓取 adb shell ip route list table 0 | grep "local 192.168."
        // 跳转wifi adb shell am start com.android.settings/.wifi.WifiPickerActivity
        val jButton = Button(ADBWirelessStepsBtn[0].toString(), 10, 200, 310, 50)
        jPanel.add(jButton)
        jButton.addActionListener {
            page++
            jTextArea.text = ADBWirelessStepsText[page].asString
            jButton.text = ADBWirelessStepsBtn[page].toString()
            when (page) {
                1 -> {
                    val list =
                        runAdbGetList("shell am start com.android.settings/.wifi.WifiPickerActivity")
                    if (list.contains("exception") || list.contains("error")) {
                        jTextArea.text = jsonObject.get("connect_fail").asString
                        return@addActionListener
                    }
                    jTextArea.append(jsonObject.get("succeed").asString)
                }
                2 -> {
                    runAdb("tcpip 5555")
                    jTextArea.append(jsonObject.get("confirm").asString)
                }
                3 -> {
                    Thread {

                        val text: String
                        val ip = StringBuilder("")
                        try {
                            text =
                                runAdbGetList("shell ip route list table 0 | grep \"192.168.\" | grep local | grep -v broadcast")[0]
                            print(text)
                            var i = text.indexOf("192")
                            print(i)

                            while (text[i].toString() != " ") {
                                ip.append(i)
                                i++
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        /* 192.168.1.[0-9]{1,3} */
                        if (!ip.toString().matches("192.168.1.[0-9]{1,3}".toRegex())) {
                            jTextArea.append(jsonObject.get("fetch_fail").asString)
                            val jTextField = JTextField()
                            jPanel.add(jTextField)
                            jTextField.setBounds(5, 150, 250, 25)

                            val confirm = Button(jsonObject.get("confirm").asString, 255, 150, 70, 25)
                            jPanel.add(confirm)
                            confirm.isVisible = false
                            confirm.addActionListener {
                                ip.clear()
                                ip.append(jTextField.text)
                                jTextArea.append(String.format(jsonObject.get("ip_input").asString, ip))
                                connectToIp(ip.toString(), jTextArea)
                                confirm.isVisible = false
                                jTextField.isVisible = false
                            }
                            confirm.isVisible = true
                        } else {
                            jTextArea.append(String.format(jsonObject.get("fetch_succeed").asString, ip))
                            connectToIp(ip.toString(), jTextArea)
                        }
                    }.start()
                }
                5 -> {
                    if (!MainWindow.isCreated()){
                        ControlCenter.getInstance().controlListener.onConfirmConnection()
                    }
                    dispose()
                }
            }
        }
        jPanel.isVisible = true
    }

    private fun connectToIp(ip: String, jTextArea: JTextArea) {
        if (runAdbGetText("connect $ip:5555").contains("connected")) {
            jTextArea.append(jsonObject.get("connect_succeed").asString)
        } else {
            jTextArea.append(String.format(jsonObject.get("fetch_failed").asString, ip))
        }
    }
}