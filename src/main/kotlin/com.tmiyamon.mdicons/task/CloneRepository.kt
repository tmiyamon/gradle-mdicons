package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import com.tmiyamon.mdicons.ext.getExtensionOf
import com.tmiyamon.mdicons.repository.MaterialDesignIcons
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import java.io.File

open class CloneRepository() : DefaultTask() {
    init {
        val project = getProject()
        val cacheDir = File(Extension.CACHE_PATH)
        val repository = MaterialDesignIcons(cacheDir)

        project.afterEvaluate {
            onlyIf{ !cacheDir.isDirectory() }
            doLast {
                repository.gitClone()
            }
        }
    }
}
