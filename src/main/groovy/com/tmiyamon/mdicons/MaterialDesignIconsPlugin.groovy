package com.tmiyamon.mdicons

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tmiyamon.mdicons.task.SyncRepositoryTask
import com.tmiyamon.mdicons.task.UninstallAssetsTask
import com.tmiyamon.mdicons.task.InstallAssetsTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class MaterialDesignIconsPlugin implements Plugin<Project> {
    static final String NAME = "mdicons"
    static final String GROUP = "MaterialDesignIcons"

    @Override
    void apply(Project project) {
        def assets = project.container(AssetTarget) {
            String assetName = it.toString()
            project.extensions.create(it, AssetTarget, assetName)
        }

        def mdicons = new MaterialDesignIconsExtension(assets)
        project.convention.plugins.mdicons = mdicons
        project.extensions.mdicons = mdicons

        def syncRepositoryTask = SyncRepositoryTask.createTask(project)
        def installAssetsTask = InstallAssetsTask.createTask(project)
        def uninstallAssetsTask = UninstallAssetsTask.createTask(project)

        installAssetsTask.dependsOn(uninstallAssetsTask, syncRepositoryTask)

        project.plugins.withType(AppPlugin) {
            project.android.sourceSets.main.res.srcDirs += AndroidProject.RES_RELATIVE_PATH
        }
        project.plugins.withType(LibraryPlugin) {
            project.android.sourceSets.main.res.srcDirs += AndroidProject.RES_RELATIVE_PATH
        }
    }
}
