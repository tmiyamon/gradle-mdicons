package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.AndroidProject
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction


class UninstallAssetsTask extends DefaultTask {
    @TaskAction
    def run() {
        AndroidProject.newWithGradleProject(project).resDir.deleteDir()
    }

    static def createTask(Project project) {
        project.task(
            type: UninstallAssetsTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Uninstall assets from your project",
            "uninstallAssets"
        )
    }
}
