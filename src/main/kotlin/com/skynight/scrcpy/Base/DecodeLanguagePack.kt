package com.skynight.scrcpy.Base

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File

class DecodeLanguagePack private constructor(){
    private var languageList: MutableList<String>
    private lateinit var locale: String
    private lateinit var jsonObject: JsonObject

    companion object {
        const val path = "language"
        @Volatile
        private var instance: DecodeLanguagePack? = null
        @Synchronized
        fun getInstance(): DecodeLanguagePack {
            if (instance == null) {
                instance = DecodeLanguagePack()
            }
            return instance as DecodeLanguagePack
        }
    }
    init {
        val file = File(System.getProperty("user.dir") + File.separator + path)
        languageList = file.list().toMutableList()
    }

    fun setLocale(region: String = "zh", language: String = "rCN"): DecodeLanguagePack {
        return setLocale("$region-$language")
    }
    fun setLocale(locale: String = "zh-rCN"): DecodeLanguagePack {
        this.locale = locale
        return this
    }
    fun decode() {
        if (languageList.contains(locale)) {
            println(System.getProperty("user.dir") + File.separator + path + File.separator + locale)
            jsonObject = JsonParser().parse(File(System.getProperty("user.dir") + File.separator + path + File.separator + locale).inputStream().bufferedReader().readText()) as JsonObject
        } else {
            throw Exception("LocaleNotSpecifiedException")
        }
    }

    fun getJsonObject(): JsonObject {
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