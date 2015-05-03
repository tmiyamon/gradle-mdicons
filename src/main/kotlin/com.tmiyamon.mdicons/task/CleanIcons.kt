package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import com.tmiyamon.mdicons.ext.getExtensionOf
import org.gradle.api.Project
import org.gradle.api.Task


class CleanIcons(val task: Task) : TaskLike {
    override fun doOnAfterEvaluate(evaluator: Evaluator) {
        val ext = getExtensionOf(task.getProject())

        task.getInputs().properties(mapOf(
            "patterns" to ext.patterns,
            "groups" to ext.groups
        ))
        task.getOutputs().upToDateWhen{ true }
        task.doLast {
            Extension.loadPreviousConfig(task.getProject()).results.forEach {
                if(task.getProject().file(it.dstPath).delete()) {
                    MaterialDesignIconsPlugin.logger.info("Removed ${it.dstPath} (added in ${it.taskName})")
                }
            }
        }
    }
}

