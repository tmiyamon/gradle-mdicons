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

class UninstallAssetTask extends DefaultTask {
    String assetName

    @TaskAction
    def run() {
        def asset = Asset.findOf(project, assetName)
        def androidProject = AndroidProject.newWithGradleProject(project)
        def repository = MaterialDesignIcons.newWithRootDir()
        repository.uninstallFrom(asset, androidProject)
    }

    static def createTask(Project project, String assetName) {
        project.task(
            type: UninstallAssetTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Uninstall asset $assetName from your project",
            "uninstallAsset${Utils.pascalize(assetName)}"
        ) {
            it.assetName = assetName
        }
    }

    static def createComprehensionTask(Project project, List<Task> dependsOn) {
        project.task(
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Uninstall all assets from your project",
            dependsOn: dependsOn,
            "uninstallAssets"
        )
    }
}
