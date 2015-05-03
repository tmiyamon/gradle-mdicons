package com.tmiyamon.mdicons.mdicons.tasks

import org.gradle.api.Task
import org.gradle.api.logging.LogLevel

import static com.tmiyamon.mdicons.mdicons.MaterialDesignIconsPlugin.NAME
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
abstract class AbstractTaskWrapper implements ITaskWrapper {
    Task task

    protected AbstractTaskWrapper(Project project) {
        this.task = project.task("${NAME}${taskName}").configure {
            logging.captureStandardOutput LogLevel.DEBUG
            logging.captureStandardError LogLevel.ERROR
        }
    }

    String getTaskName() {
        getClass().simpleName
    }

    def dependsOn(ITaskWrapper ... dependingTasks) {
        task.dependsOn dependingTasks*.task
        this
    }
}
