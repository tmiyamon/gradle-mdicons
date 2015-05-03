package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import com.tmiyamon.mdicons.Result
import com.tmiyamon.mdicons.ext.getExtensionOf
import com.tmiyamon.mdicons.ext.info
import com.tmiyamon.mdicons.ext.slice
import com.tmiyamon.mdicons.ext.tap
import com.tmiyamon.mdicons.repository.MaterialDesignIcons
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import java.io.File
import java.io.FileFilter
import java.util.regex.Pattern

open class CopyIconsByPattern() : DefaultTask() {
    init {
        val project = getProject()
        val repository = MaterialDesignIcons(File(Extension.CACHE_PATH))

        project.afterEvaluate {
            val ext = getExtensionOf(project)

            onlyIf{ ext.patterns.isNotEmpty() }
            getInputs().properties(ext.toMap(Extension.KEY_PATTERNS))
            getOutputs().upToDateWhen{ true }

            doLast {
                val pattern = ext.buildPattern()
                info("matching pattern: $pattern")

                val results = repository.copyIconFileMatch(project, FileFilter {
                    it.name.matches(pattern)
                })

                ext.results.addAll(results.map {
                    Result(it.getSourceRelativePath(), it.getDestinationRelativePath(), getName())
                })
            }
        }
    }
}
