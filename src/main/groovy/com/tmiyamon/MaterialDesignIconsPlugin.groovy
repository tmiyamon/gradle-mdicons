package com.tmiyamon

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

/**
 * Created by tmiyamon on 12/24/14.
 */

class MaterialDesignIconsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task('fetchMaterialDesignIcons') {
            def savedDir = new File("${System.getProperty("user.home")}/.material_design_icons")
            def repoUrl = 'git@github.com:google/material-design-icons.git'
            def resDirName = 'src/main/res'

            if (!savedDir.isDirectory()) {
                def command = "git clone ${repoUrl} ${savedDir.getAbsolutePath()}"
                command.execute().waitFor()
            }
            if (savedDir.isDirectory()) {

                def resDir = new File(project.getProjectDir(), resDirName)

                if (!resDir.isDirectory()) {
                    resDir.mkdir();
                }

                savedDir.traverse {
                    if (it.isFile() && it.getParentFile() != null &&
                            it.getParentFile().getName().startsWith("drawable") &&
                            it.getName().contains("search")
                    ) {
                        def iconFile = it
                        def drawableTypeDir = iconFile.getParentFile().getName()
                        def drawableDir = new File(resDir, drawableTypeDir)
                        if (!drawableDir.isDirectory()) {
                            drawableDir.mkdir()
                        }

                        if (!new File(drawableDir, iconFile.getName()).exists()) {
                            project.logger.debug("copy from ${iconFile} to ${drawableDir}")

                            project.copy {
                                from iconFile
                                into drawableDir
                            }
                        }
                    }
                }
            }
        }

        project.plugins.withType(ApplicationPlugin) {
            project.tasks.findByName('run').dependsOn('fetchMaterialDesignIcons')
        }
    }
}