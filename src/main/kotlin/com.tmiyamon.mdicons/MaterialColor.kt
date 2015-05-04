package com.tmiyamon.mdicons

import com.google.gson.Gson
import groovy.json.JsonSlurper
import java.io.BufferedReader
import java.io.InputStreamReader

object MaterialColor {
    val FILE_NAME = "com/tmiyamon/mdicons/colors.json"

    val colors = loadColors()

    fun loadColors(): Map<String, String> {
        val c = javaClass<MaterialDesignIconsPlugin>()
        val reader = InputStreamReader(c.getClassLoader().getResourceAsStream(FILE_NAME))

        return reader.use {
            Gson().fromJson(it, javaClass<Map<String, String>>())
        }
    }

    fun getHexFrom(color: String): String {
        val colorHex = colors[color]
        if (colorHex == null) {
            throw IllegalArgumentException("Color not supported: ${color}. See the list in https://github.com/tmiyamon/gradle-mdicons/blob/master/src/main/resources/com/tmiyamon/colors.json")
        }
        return colorHex
    }
}
