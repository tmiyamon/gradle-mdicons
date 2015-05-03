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
    val groups = arrayListOf<String>()
    val results = arrayListOf<Result>()

    fun toMap(): Map<String, Any> {
        return mapOf(
            "patterns" to patterns,
            "groups" to groups,
            "results" to results
        )
    }

    fun toJson(): String {
        return JsonBuilder(toMap()).toString()
    }

    fun save(project: Project): Unit {
        project.file(FILENAME).withWriter { writer ->
            writer.write(toJson())
        }
    }

    fun isChanged(project: Project): Boolean {
        val prevConfig = project.file(FILENAME)
        if (prevConfig.exists()) {
            try {
                return toMap() != JsonSlurper().parse(prevConfig)
            } catch (e: Exception) {
                project.getLogger().warn("Ignore previous mdicons configuration due to its corruption")
                return true
            }
        }
        return true
    }

    fun pattern(p: String): Unit {
        if (p.isNotEmpty()) {
            patterns.add(p)
        }
    }

    fun buildPattern(): String {
        return ".*(${patterns.join("|")}).*"
    }
}

