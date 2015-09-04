package com.tmiyamon.mdicons

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tmiyamon.mdicons.task.SyncRepositoryTask
import com.tmiyamon.mdicons.task.UninstallAssetTask
import com.tmiyamon.mdicons.task.InstallAssetTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class MaterialDesignIconsPlugin implements Plugin<Project> {
    static final String NAME = "mdicons"
    static final String GROUP = "MaterialDesignIcons"

    @Override
    void apply(Project project) {
        def installTasks = []
        def uninstallTasks = []

        def assets = project.container(AssetTarget) {
            String assetName = it.toString()

            def installTask = InstallAssetTask.createTask(project, assetName)
            def uninstallTask = UninstallAssetTask.createTask(project, assetName)
            installTask.dependsOn(uninstallTask)

            installTasks << installTask
            uninstallTasks <<  uninstallTask

            project.extensions.create(it, AssetTarget, assetName)
        }

        def mdicons = new MaterialDesignIconsExtension(assets)
        project.convention.plugins.mdicons = mdicons
        project.extensions.mdicons = mdicons

        SyncRepositoryTask.createTask(project)

        InstallAssetTask.createComprehensionTask(project, installTasks)
        UninstallAssetTask.createComprehensionTask(project, uninstallTasks)

        project.plugins.withType(AppPlugin) {
            project.android.sourceSets.main.res.srcDirs += AndroidProject.RES_RELATIVE_PATH
        }
        project.plugins.withType(LibraryPlugin) {
            project.android.sourceSets.main.res.srcDirs += AndroidProject.RES_RELATIVE_PATH
        }
    }
}
