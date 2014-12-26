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
            def resourceDir = project.file(project.mdicons.resourcePath)

            def iconTypes = [
                'action','alert','av','communication','content','device',
                'editor','file','hardware','image','maps','navigation',
                'notification','social','toggle'
            ] as Set

            def cloneRepository = project.task('cloneRepository') {
                if (!cacheDir.isDirectory()) {
                    def command = "git clone ${repoUrl} ${cacheDir.getAbsolutePath()}"
                    command.execute().waitFor()
                }
            }

            def cleanIcons = project.task('cleanIcons', dependsOn: 'cloneRepository') {
                if (cacheDir.isDirectory()) {
                    eachIconFiles(cacheDir, iconTypes, null) { cachedIconFile ->
                        def projectTypedDrawableDir = new File(resourceDir, cachedIconFile.getParentFile().getName())

                        def projectResourceFile = new File(projectTypedDrawableDir, cachedIconFile.getName())
                        if (projectResourceFile.exists()) {
                           projectResourceFile.delete()
                        }
                    }
                }
            }

            if (!project.mdicons.isConfigChanged(project)) {
                project.task('copyIcons', dependsOn: [cloneRepository, cleanIcons]) {
                    if (cacheDir.isDirectory() && pattern != null) {
                        if (!resourceDir.isDirectory()) {
                            resourceDir.mkdir();
                        }

                        eachIconFiles(cacheDir, iconTypes, pattern) { cachedIconFile ->
                            def projectTypedDrawableDir = new File(resourceDir, cachedIconFile.getParentFile().getName())
                            if (!projectTypedDrawableDir.isDirectory()) {
                                projectTypedDrawableDir.mkdir()
                            }

                            if (!new File(projectTypedDrawableDir, cachedIconFile.getName()).exists()) {
                                project.copy {
                                    from cachedIconFile
                                    into projectTypedDrawableDir
                                }
                            }
                        }
                    }

                    project.mdicons.save(project)
                }
            }
        }
    }

    /**
     * Traverse material design icons repository.
     * The structure is expected as below
     *
     * /iconType/drawable-x/ic_x.png
     *
     * @param root
     * @param iconTypes
     * @param pattern
     * @param closure
     * @return
     */
    def eachIconFiles(File root, Set iconTypes, String pattern, Closure closure) {
        root.eachDirMatch({ iconTypes.contains(new File(it).getName()) }) {
            it.eachDirMatch({ new File(it).getName().startsWith("drawable")}) {
                if (pattern != null) {
                    it.eachFileMatch({ new File(it).getName() =~ pattern }, closure)
                } else {
                    it.eachFile(closure)
                }
            }
        }
    }

}

