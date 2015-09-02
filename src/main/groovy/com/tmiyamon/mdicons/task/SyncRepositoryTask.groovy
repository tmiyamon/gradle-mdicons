package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.MaterialDesignIcons
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class SyncRepositoryTask extends DefaultTask {
    @TaskAction
    def run() {
        def repo = MaterialDesignIcons.newWithRootDir()
        if(repo.gitClone() == 128) {
            repo.gitPull()
        }
        repo.saveIndex()
    }

    static def createTask(Project project) {
        project.task(
            type: SyncRepositoryTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Clone material design icons repository or pull if already exists",
            "syncRepository"
        )
    }
}
