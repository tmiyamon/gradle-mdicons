package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.repository.MaterialDesignIcons
import org.gradle.api.Task

class CloneRepository(val task: Task) : TaskLike {
    override fun doOnAfterEvaluate(evaluator: Evaluator) {
        val repository = MaterialDesignIcons(evaluator.cacheDir)

        task.getOutputs().dir(evaluator.cacheDir)
        task.doLast {
            repository.gitClone()
        }
    }
}
