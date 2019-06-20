package com.skynight.scrcpy

import com.skynight.scrcpy.base.BaseIndex.Companion.DataSave
import com.skynight.scrcpy.base.BaseIndex.Companion.DefaultLocale
import com.skynight.scrcpy.base.BaseIndex.Companion.LanguageDir
import com.skynight.scrcpy.base.BaseIndex.Companion.SaveLocale
import com.skynight.scrcpy.base.ControlCenter
import com.skynight.scrcpy.base.LoadLanguage
import com.skynight.scrcpy.widgets.Button
import com.skynight.scrcpy.widgets.Panel
import java.awt.Toolkit
import java.io.File
import java.io.FileWriter
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel

class SelectLanguageWindow: JFrame() {
    init {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val loadLanguage = LoadLanguage.instance

        loadLanguage.setLocale(
            if (File(LanguageDir + File.separator + LoadLanguage.getSystemLocale()).exists()) {
               LoadLanguage.getSystemLocale()
            } else {
                DefaultLocale
            }
        )

        val loadJson = loadLanguage.getWindowStrings("SelectLanguageWindow")
        title = loadJson.get("title").asString
        setSize(350, 170)
        isResizable = false
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2)
        defaultCloseOperation = if (MainWindow.isCreated()) JFrame.EXIT_ON_CLOSE else JFrame.DISPOSE_ON_CLOSE

        val mainPanel = Panel(width, height, null)
        add(mainPanel)

        val l = JLabel(loadJson.get("language").asString, JLabel.CENTER)
        mainPanel.add(l)
        l.setBounds(0,0,width - 16, 20)
        val lang = JComboBox<String>()
        mainPanel.add(lang)
        lang.setBounds(10,20,width - 20 - 16, 20)
        for (i in loadLanguage.supportedLanguages) {
            lang.addItem(i)
        }

        val r = JLabel(loadJson.get("region").asString, JLabel.CENTER)
        mainPanel.add(r)
        r.setBounds(0,45,width - 16, 20)
        val reg = JComboBox<String>()
        mainPanel.add(reg)
        reg.setBounds(10,65,width - 20 - 16, 20)
        for (i in loadLanguage.supportedLocale[lang.selectedItem as String]!!) {
            reg.addItem(i)
        }
        lang.addActionListener {
            Thread {
                reg.removeAllItems()
                for (i in loadLanguage.supportedLocale[lang.selectedItem as String]!!) {
                    reg.addItem(i)
                }
            }.start()
        }

        val confirm = Button(loadJson.get("confirm").asString, 10, 95, width - 20 - 16, 30)
        mainPanel.add(confirm)
        confirm.addActionListener {
            Thread {
                //println(lang.selectedItem as String + "-r" + reg.selectedItem as String)
                LogOutputWindow.takeLog("Language: " + lang.selectedItem as String + "-r" + reg.selectedItem as String)

                val file = File(DataSave, "locale")
                if (file.exists()) {
                    file.delete()
                }
                file.createNewFile()
                val fileWriter = FileWriter(file)

                fileWriter.write(String.format(SaveLocale, lang.selectedItem as String, reg.selectedItem as String))
                fileWriter.flush()
                fileWriter.close()

                ControlCenter.getInstance().getControlListener().checkUserSave(true)

                dispose()
            }.start()
        }

        isVisible = true
    }
}
