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

        project.afterEvaluate {
            def repoUrl = 'git@github.com:google/material-design-icons.git'
            def cacheDir = new File(project.mdicons.cachePath);

            if (!cacheDir.isDirectory()) {
                def command = "git clone ${repoUrl} ${cacheDir.getAbsolutePath()}"
                command.execute().waitFor()
            }
            if (cacheDir.isDirectory()) {

                def resDir = new File(project.getProjectDir(), project.mdicons.resourceRelativePath)

                if (!resDir.isDirectory()) {
                    resDir.mkdir();
                }

                def pattern = project.mdicons.pattern
                if (pattern != null) {
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

