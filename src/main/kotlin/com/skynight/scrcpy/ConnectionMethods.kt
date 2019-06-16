package com.skynight.scrcpy

import com.skynight.scrcpy.BaseIndex.Companion.ADBWirelessStepsBtn
import com.skynight.scrcpy.BaseIndex.Companion.ADBWirelessStepsText
import com.skynight.scrcpy.widgets.Button
import java.awt.Color
import java.awt.Toolkit
import java.lang.StringBuilder
import javax.swing.*

class ADBWiredConnection : JFrame("USB有线连接") {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize

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
        val jTextArea = JTextArea(
            "请于开发者选项中打开ADB调试, 然后使用USB连接计算机\n" +
                    "开发者选项打开方式:\n" +
                    "设置 → 关于{设备/手机} →\n" +
                    "\n" +
                    "MIUI\n" +
                    " - 多次点击\"MIUI版本\" → 返回 → 更多设置 → 开发者选项\n" +
                    "原生/类原生 Pie\n" +
                    " - 多次点击版本号 → 系统 → 高级 → 开发者选项\n"
        )
        jPanel.add(jTextArea)
        jTextArea.isEditable = false
        jTextArea.setBounds(5, 0, 350, 184)

        val jButton = Button("已按步骤完成", 10, 200, 310, 50)
        jButton.addActionListener {
            ControlCenter.getInstance().controlListener.onConfirmConnection()
            dispose()
        }
        jPanel.add(jButton)

        jPanel.isVisible = true
    }
}

class ADBWirelessConnection : JFrame("通过WiFi使用TCP/IP连接") {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        setSize(350, 300)
        setLocation((screenSize.width - 300) / 2, (screenSize.height - 120) / 2)
        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        isVisible = true

        val jPanel = JPanel()
        add(jPanel)
        jPanel.isVisible = false
        jPanel.background = Color.WHITE
        jPanel.layout = null
        jPanel.setSize(350, 300)

        var page = 0
        val jTextArea = JTextArea(ADBWirelessStepsText[0])
        jPanel.add(jTextArea)
        jTextArea.isEditable = false
        jTextArea.setBounds(5, 0, 350, 150)

        // IP 抓取 adb shell ip route list table 0 | grep "local 192.168."
        // 跳转wifi adb shell am start com.android.settings/.wifi.WifiPickerActivity
        val jButton = Button(ADBWirelessStepsBtn[0], 10, 200, 310, 50)
        jPanel.add(jButton)
        jButton.addActionListener {
            page++
            jTextArea.text = ADBWirelessStepsText[page]
            jButton.text = ADBWirelessStepsBtn[page]
            when (page) {
                1 -> {
                    val list = runAdbGetList("shell am start com.android.settings/.wifi.WifiPickerActivity")
                    if (list.contains("exception") || list.contains("error")) {
                        jTextArea.text = "失败, 请手动打开并设置"
                        return@addActionListener
                    }
                    jTextArea.append("完成")
                }
                2 -> {
                    runAdb("tcpip 5555")
                    jTextArea.append("完成")
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
                            jTextArea.append("获取失败, 请手动输入\n")
                            val jTextField = JTextField()
                            jPanel.add(jTextField)
                            jTextField.setBounds(5, 150, 250, 25)

                            val confirm = Button("确定", 255, 150, 70, 25)
                            jPanel.add(confirm)
                            confirm.isVisible = false
                            confirm.addActionListener {
                                ip.clear()
                                ip.append(jTextField.text)
                                jTextArea.append("输入的IP为: $ip\n")
                                connectToIp(ip.toString(), jTextArea)
                                confirm.isVisible = false
                                jTextField.isVisible = false
                            }
                            confirm.isVisible = true
                        } else {
                            jTextArea.append("获取成功: $ip\n")
                            connectToIp(ip.toString(), jTextArea)
                        }
                    }.start()
                }
                5 -> {
                    ControlCenter.getInstance().controlListener.onConfirmConnection()
                    dispose()
                }
            }
        }
        jPanel.isVisible = true
    }

    private fun connectToIp(ip: String, jTextArea: JTextArea) {
        if (runAdbGetText("connect $ip:5555").contains("connected")) {
            jTextArea.append("与设备利用局域网连接成功")
        } else {
            jTextArea.append("请尝试使用adb手动连接, 命令如下:\nadb connect $ip:5555")
        }
    }

}