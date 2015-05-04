package com.tmiyamon.mdicons

import com.google.gson.Gson
import java.io.File
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.api.Project
import com.tmiyamon.mdicons.ext.*

open class Extension() {
    companion object {
        val FILENAME = ".mdicons"
        val PROJECT_RESOURCE_RELATIVE_PATH = pathJoin("src", "main", "res")
        val CACHE_PATH = pathJoin(System.getProperty("user.home"), ".material_design_icons")

        val KEY_PATTERNS = "patterns"
        val KEY_ASSETS = "assets"
        val KEY_RESULTS = "results"
        val SUPPORTED_KEYS_FOR_MAPPING = array(KEY_PATTERNS, KEY_ASSETS, KEY_RESULTS)

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
    val assets = arrayListOf<Asset>()
    val results = arrayListOf<Result>()


    fun toMap(
        vararg keys: String = SUPPORTED_KEYS_FOR_MAPPING
    ): Map<String, Any> {
        return keys.filter{ SUPPORTED_KEYS_FOR_MAPPING.contains(it) }.toMapWith { key ->
            when(key) {
                KEY_PATTERNS -> patterns
                KEY_ASSETS -> assets
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

    fun asset(map: Map<String, Any>) {
        val keys = array("name", "color", "size")

        if (map.keySet().intersect(setOf(*keys)).size() != keys.size()) {
            warn("Failed to apply parameters(${map}) to asset. All params of name, color and size must be given")
            return
        }

        val (names, colors, sizes) = map.valuesAt(*keys).map {
            when(it) {
                is Array<*> -> it.map { it.toString() }.copyToArray()
                is List<*> -> it.map { it.toString() }.copyToArray()
                is String -> array(it)
                else -> null
            }
        }

        if (array(names, colors, sizes).any { it == null }) {
            warn("Failed to apply parameters(${map}) to asset. All values must be non-null String, Array or List")
            return
        }

        assets.add(Asset(
                names as Array<String>,
                colors as Array<String>,
                sizes as Array<String>
        ))
    }

    fun buildPattern(): String {
        return ".*(${patterns.join("|")}).*"
    }

}

