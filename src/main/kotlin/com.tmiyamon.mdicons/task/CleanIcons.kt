package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import com.tmiyamon.mdicons.ext.getExtensionOf
import com.tmiyamon.mdicons.ext.slice
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task


open class CleanIcons() : DefaultTask() {
    init {
        val project = getProject()
        project.afterEvaluate {
            val ext = getExtensionOf(project)
            val inputsProperties = ext.toMap(
                    Extension.KEY_PATTERNS, Extension.KEY_ASSETS)

            getInputs().properties(inputsProperties)
            getOutputs().upToDateWhen{ true }
            doLast {
                Extension.loadPreviousConfig(project).results.forEach {
                    if(project.file(it.dstPath).delete()) {
                        MaterialDesignIconsPlugin.logger.info("Removed ${it.dstPath} (added in ${it.taskName})")
                    }
                }
            }
        }
    }
}

