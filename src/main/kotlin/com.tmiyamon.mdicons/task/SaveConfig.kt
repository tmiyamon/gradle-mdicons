package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.ext.getExtensionOf
import org.gradle.api.DefaultTask
import org.gradle.api.Task

open class SaveConfig() : DefaultTask() {
    init {
        val project = getProject()
        project.afterEvaluate {
            val ext = getExtensionOf(project)
            doLast {
                ext.save(project)
            }
        }
    }
}

