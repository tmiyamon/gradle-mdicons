package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.ColorSet
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class ListColorsTask extends DefaultTask {
    @TaskAction
    def run() {
        println ColorSet.populate(project).toPrettyString()
    }

    static def createTask(Project project) {
        project.task(
            type: ListColorsTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "List color set",
            "listColors"
        )
    }
}
