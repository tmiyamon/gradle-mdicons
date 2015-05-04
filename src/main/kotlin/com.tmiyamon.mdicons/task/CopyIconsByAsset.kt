package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.Result
import com.tmiyamon.mdicons.ext.getExtensionOf
import com.tmiyamon.mdicons.ext.info
import com.tmiyamon.mdicons.repository.MaterialDesignIcons
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import java.io.File

open class CopyIconsByAsset() : DefaultTask() {
    init {
        val project = getProject()
        val repository = MaterialDesignIcons(File(Extension.CACHE_PATH))

        project.afterEvaluate {
            val ext = getExtensionOf(project)

            onlyIf{ ext.assets.isNotEmpty() }
            getInputs().properties(ext.toMap(Extension.KEY_ASSETS))
            getOutputs().upToDateWhen{ true }

            doLast {
                val results = repository.copyIconFileMatchAsset(project, ext.assets)

                ext.results.addAll(results.map {
                    Result(it.getSourceRelativePath(), it.getDestinationRelativePath(), getName())
                })
            }
        }
    }

}

