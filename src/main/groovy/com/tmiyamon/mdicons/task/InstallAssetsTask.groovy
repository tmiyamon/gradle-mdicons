package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.AndroidProject
import com.tmiyamon.mdicons.Asset
import com.tmiyamon.mdicons.MaterialDesignIcons
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class InstallAssetsTask extends DefaultTask {
    String assetName

    @TaskAction
    def run() {
        def assets = Asset.allOf(project)
        def androidProject = AndroidProject.newWithGradleProject(project)
        def repository = MaterialDesignIcons.newWithRootDir()
        repository.installTo(assets, androidProject)
    }

    static def createTask(Project project) {
        project.task(
            type: InstallAssetsTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Install assets into your project",
            "installAssets"
        )
    }
}
