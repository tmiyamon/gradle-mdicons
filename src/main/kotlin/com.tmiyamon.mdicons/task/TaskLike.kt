package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Evaluator
import org.gradle.api.Task

trait TaskLike {
    fun doOnAfterEvaluate(evaluator: Evaluator)
}

