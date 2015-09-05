package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.AndroidProject
import com.tmiyamon.mdicons.Asset
import com.tmiyamon.mdicons.Installer
import com.tmiyamon.mdicons.MaterialDesignColor
import com.tmiyamon.mdicons.MaterialDesignIconsRepository
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class InstallAssetsTask extends DefaultTask {
    String assetName

    @TaskAction
    def run() {
        def assets = Asset.allOf(project)
        def targetProject = AndroidProject.build(project)
        def repository = MaterialDesignIconsRepository.build()
        def supportedColors = MaterialDesignColor.populate(project)

        Installer.create(assets, repository, supportedColors, targetProject).install()
    }

    static def createTask(Project project) {
        project.task(
            type: InstallAssetsTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "Install assets into your targetProject",
            "installAssets"
        )
    }
}
