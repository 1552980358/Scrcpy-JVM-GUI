package com.skynight.scrcpy.base

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.util.*

class LoadLanguage {
    private lateinit var languageList: MutableList<String>
    private lateinit var jsonObject: JsonObject

    private lateinit var locale: String
    lateinit var language: String
    lateinit var region: String

    val supportedLanguages = mutableListOf<String>()
    private val supportedLocale = mutableMapOf<String, MutableList<String>>()

    fun getSupportedLocale(): MutableMap<String, MutableList<String>> {
        return supportedLocale
    }

    companion object {
        private lateinit var systemLanguage: String
        private lateinit var systemRegion: String
        fun getSystemLocale(): String {
            if (!::systemLanguage.isInitialized || !::systemRegion.isInitialized) {
                val locale = Locale.getDefault()
                systemLanguage = locale.language
                systemRegion = locale.country
            }
            return "$systemLanguage-r$systemRegion"
        }
/*
        @Volatile
        private var instance: LoadLanguage? = null
        @Synchronized
        fun getInstance(): LoadLanguage {
            if (instance == null) {
                instance = LoadLanguage()
            }
            return instance as LoadLanguage
        }

 */
    }

    init {

    }

    fun start() {
        val file = File(System.getProperty("user.dir") + File.separator + "language")
        languageList = file.list().toMutableList()

        val loadLanguages = Thread {
            val json = JsonParser().parse(File(System.getProperty("user.dir") + File.separator + "package" + File.separator + "SupportedLanguage").inputStream().bufferedReader().readText()).asJsonObject
            for (i in json.get("Languages").asJsonArray) {
                supportedLanguages.add(i.asString)
                //println(i.toString())
                //val a = i.toString() val k = JsonParser().parse(File(System.getProperty("user.dir") + File.separator + "package" + File.separator + "SupportedLanguage").inputStream().bufferedReader().readText()).asJsonObject
                val tmp = mutableListOf<String>()
                for (j in  json.get(i.asString).asJsonArray) {
                    tmp.add(j.asString)
                }
                supportedLocale[i.asString] = tmp
            }
        }
        loadLanguages.start()
        val path = File(BaseIndex.DataSave + File.separator + "locale")
        if (path.exists()) {
            try {
                val j = path.inputStream().bufferedReader().readText()
                val json = JsonParser().parse(j).asJsonObject
                val region = json.get("region").asString
                val language = json.get("language").asString


                setLocale(language, region)
                ControlCenter.getInstance().getControlListener().checkUserSave(true)
            } catch (e: Exception) {
                e.printStackTrace()
                path.delete()
                while (loadLanguages.isAlive) {
                    try {
                        Thread.sleep(1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                ControlCenter.getInstance().getControlListener().checkUserSave(false)
            }
        } else {
            while (loadLanguages.isAlive) {
                try {
                    Thread.sleep(1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            ControlCenter.getInstance().getControlListener().checkUserSave(false)
        }
        println("init finish")
    }

    fun setLocale(language: String, region: String) {
        this.language = language
        this.region = region
        this.setLocale("$language-r$region")
    }
    fun setLocale(locale: String) {
        println(locale)
        this.locale = locale
        loadLocaleFile()
    }
    private fun loadLocaleFile() {
        if (languageList.contains(locale)) {
            println(System.getProperty("user.dir") + File.separator + "language" + File.separator + locale)
            jsonObject = JsonParser().parse(File(System.getProperty("user.dir") + File.separator + "language" + File.separator + locale)
                .inputStream().bufferedReader().readText()).asJsonObject
        } else {
            throw Exception("LocaleNotSpecifiedException")
        }
    }

    private fun getJsonObject(): JsonObject {
        if (::jsonObject.isInitialized) {
            return jsonObject
        } else {
            throw Exception("JsonObjectNotInitializeExcrption")
        }
    }
    fun getWindowStrings(window: String): JsonObject {
        return getJsonObject().get(window).asJsonObject
    }
}