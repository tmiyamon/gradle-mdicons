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

        SyncRepositoryTask.createTask(project)

        def installTask = InstallAssetsTask.createTask(project)
        def uninstallTask = UninstallAssetsTask.createTask(project)
        installTask.dependsOn(uninstallTask)

        project.plugins.withType(AppPlugin) {
            project.android.sourceSets.main.res.srcDirs += AndroidProject.RES_RELATIVE_PATH
        }
        project.plugins.withType(LibraryPlugin) {
            project.android.sourceSets.main.res.srcDirs += AndroidProject.RES_RELATIVE_PATH
        }
    }
}
