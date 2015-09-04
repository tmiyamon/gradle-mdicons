package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.AndroidProject
import com.tmiyamon.mdicons.Asset
import com.tmiyamon.mdicons.MaterialDesignIcons
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import com.tmiyamon.mdicons.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

class InstallAssetTask extends DefaultTask {
    String assetName

    @TaskAction
    def run() {
        def asset = Asset.findOf(project, assetName)
        def androidProject = AndroidProject.newWithGradleProject(project)
        def repository = MaterialDesignIcons.newWithRootDir()
        repository.installTo(asset, androidProject)
    }

    static def createTask(Project project, String assetName) {
        project.task(
            type: InstallAssetTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Install asset $assetName into your project",
            "installAsset${Utils.pascalize(assetName)}"
        ) {
            it.assetName = assetName
        }
    }

    static def createComprehensionTask(Project project, List<Task> dependsOn) {
        project.task(
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Install all assets into your project",
            dependsOn: dependsOn,
            "installAssets"
        )
    }
}
