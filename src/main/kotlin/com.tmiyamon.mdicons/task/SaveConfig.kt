package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.ext.getExtensionOf
import org.gradle.api.Task

class SaveConfig(val task: Task) : TaskLike {
    override fun doOnAfterEvaluate(evaluator: Evaluator) {
        task.doLast {
            val ext = getExtensionOf(task.getProject())
            ext.save(task.getProject())
        }
    }
}

