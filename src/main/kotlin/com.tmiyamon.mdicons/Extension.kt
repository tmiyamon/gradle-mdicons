package com.tmiyamon.mdicons

import com.google.gson.Gson
import java.io.File
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.api.Project
import com.tmiyamon.mdicons.ext.*

open class Extension {
    companion object {
        val FILENAME = ".mdicons"
        val PROJECT_RESOURCE_RELATIVE_PATH = pathJoin("src", "main", "res")
        val CACHE_PATH = pathJoin(System.getProperty("user.home"), ".material_design_icons")

        val KEY_PATTERNS = "patterns"
        val KEY_ASSETS = "assets"
        val KEY_RESULTS = "results"
        val SUPPORTED_KEYS_FOR_MAPPING = array(KEY_PATTERNS, KEY_RESULTS)

        fun loadPreviousConfig(project: Project): Extension {
            val prevConfig = project.file(FILENAME)
            return if (prevConfig.exists()) {
                prevConfig.withReader {
                    Gson().fromJson(it, javaClass<Extension>())
                }
            } else {
                Extension()
            }
        }
    }

    val patterns = arrayListOf<String>()
//    val assets = arrayListOf<Asset>()
    val results = arrayListOf<Result>()

    fun toMap(
        vararg keys: String = SUPPORTED_KEYS_FOR_MAPPING
    ): Map<String, Any> {
        return keys.filter{ SUPPORTED_KEYS_FOR_MAPPING.contains(it) }.toMapWith { key ->
            when(key) {
                KEY_PATTERNS -> patterns
//                KEY_ASSETS -> assets
                KEY_RESULTS -> results
                else -> {}
            }
        }
    }

    fun toJson(): String {
        return JsonBuilder(toMap()).toString()
    }

    fun save(project: Project): Unit {
        project.file(FILENAME).withWriter { writer ->
            writer.write(toJson())
        }
    }

    fun pattern(p: String): Unit {
        if (p.isNotEmpty()) {
            patterns.add(p)
        }
    }

//    fun asset(fileNamePattern: String, color: List<String>, size: List<String>) {
//        assets.add(Asset(fileNamePattern, color, size))
//    }
//    fun asset(fileNamePattern: String, color: String, size: List<String>) {
//        asset(fileNamePattern, listOf(color), size)
//    }
//    fun asset(fileNamePattern: String, color: List<String>, size: String) {
//        asset(fileNamePattern, color, listOf(size))
//    }
//    fun asset(fileNamePattern: String, color: String, size: String) {
//        asset(fileNamePattern, listOf(color), listOf(size))
//    }

    fun buildPattern(): String {
        return ".*(${patterns.join("|")}).*"
    }

}

