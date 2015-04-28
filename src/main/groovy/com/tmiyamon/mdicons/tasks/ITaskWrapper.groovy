package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import org.gradle.api.Task

/**
 * Created by tmiyamon on 4/26/15.
 */
public interface ITaskWrapper {
    Task getTask()
    def doOnAfterEvaluate(Evaluator evaluator)
}
