package com.tmiyamon.mdicons

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tmiyamon.mdicons
import com.tmiyamon.mdicons.ext.taskOf
import com.tmiyamon.mdicons.task.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class MaterialDesignIconsPlugin: Plugin<Project> {
    companion object {
        val NAME = "mdicons"
        val logger = LoggerFactory.getLogger(javaClass<MaterialDesignIconsPlugin>())
    }

    override fun apply(p: Project) {
        p.getExtensions().create(NAME, javaClass<Extension>())

        val cloneRepository    = p.taskOf(javaClass<CloneRepository>())
        val cleanIcons         = p.taskOf(javaClass<CleanIcons>())
        val copyIconsByPattern = p.taskOf(javaClass<CopyIconsByPattern>()).dependsOn(cloneRepository, cleanIcons)
        val copyIconsByGroup   = p.taskOf(javaClass<CopyIconsByGroup>()).dependsOn(cloneRepository, cleanIcons)
        val saveConfig         = p.taskOf(javaClass<SaveConfig>()).dependsOn(copyIconsByPattern, copyIconsByGroup, cleanIcons, cloneRepository)

        p.getPlugins().withType(javaClass<AppPlugin>()) {
            p.getTasks().findByName("preBuild").dependsOn(saveConfig)
        }
        p.getPlugins().withType(javaClass<LibraryPlugin>()) {
            p.getTasks().findByName("preBuild").dependsOn(saveConfig)
        }
    }
}
