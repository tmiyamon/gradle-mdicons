package com.tmiyamon

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin

/**
 * Created by tmiyamon on 12/24/14.
 */

public class MaterialDesignIconsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("mdicons", MaterialDesignIconsPluginExtension)

        project.plugins.withType(ApplicationPlugin) {
            project.tasks.findByName('run').dependsOn('copyIcons')
        }

        project.afterEvaluate {
            def repoUrl = 'git@github.com:google/material-design-icons.git'
            def cacheDir = new File(project.mdicons.cachePath);
            def pattern = project.mdicons.pattern

            project.task('cloneRepository') {
                if (!cacheDir.isDirectory()) {
                    def command = "git clone ${repoUrl} ${cacheDir.getAbsolutePath()}"
                    command.execute().waitFor()
                }
            }

            project.task('copyIcons', dependsOn: 'cloneRepository') {
                if (cacheDir.isDirectory() && pattern != null) {

                    def resDir = new File(project.getProjectDir(), project.mdicons.resourceRelativePath)

                    if (!resDir.isDirectory()) {
                        resDir.mkdir();
                    }

                    cacheDir.traverse {
                        if (it.isFile() && it.getParentFile() != null &&
                                it.getParentFile().getName().startsWith("drawable") &&
                                it.getName() =~ pattern
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
        }
    }
}

