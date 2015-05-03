package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Evaluator
import org.gradle.api.Task

class CopyIconsByGroup(val task: Task) : TaskLike {
    override fun doOnAfterEvaluate(evaluator: Evaluator) {
    }
}

