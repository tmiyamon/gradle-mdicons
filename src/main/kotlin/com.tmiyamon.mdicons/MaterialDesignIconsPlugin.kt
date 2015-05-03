package com.tmiyamon.mdicons

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tmiyamon.mdicons
import com.tmiyamon.mdicons.Evaluator
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

        val cloneRepository    = CloneRepository(p.task("${NAME}CloneRepository"))
        val cleanIcons         = CleanIcons(p.task("${NAME}CleanIcons"))
        val copyIconsByPattern = CopyIconsByPattern(p.task("${NAME}CopyIconsByPattern").dependsOn(cloneRepository.task, cleanIcons.task))
        val copyIconsByGroup   = CopyIconsByGroup(p.task("${NAME}CopyIconsByGroups").dependsOn(cloneRepository.task, cleanIcons.task))
        val saveConfig         = SaveConfig(p.task("${NAME}SaveConfig").dependsOn(copyIconsByPattern.task, copyIconsByGroup.task, cleanIcons.task, cloneRepository.task))

        val tasks = arrayListOf(cloneRepository, cleanIcons, copyIconsByPattern, copyIconsByGroup, saveConfig)

        p.getPlugins().withType(javaClass<AppPlugin>()) {
            p.getTasks().findByName("preBuild").dependsOn(saveConfig.task)
        }
        p.getPlugins().withType(javaClass<LibraryPlugin>()) {
            p.getTasks().findByName("preBuild").dependsOn(saveConfig.task)
        }

        p.afterEvaluate {
            val evaluator = Evaluator(p)
            tasks.forEach { it.doOnAfterEvaluate(evaluator) }
        }
    }
}
