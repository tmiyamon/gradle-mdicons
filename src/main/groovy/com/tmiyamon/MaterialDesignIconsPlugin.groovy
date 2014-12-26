package com.tmiyamon

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ApplicationPlugin

/**
 * Created by tmiyamon on 12/24/14.
 */

public class MaterialDesignIconsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("mdicons", MaterialDesignIconsPluginExtension)

        def cloneRepositoryTask = project.task('mdiconsCloneRepository')
        def cleanIconsTask = project.task('mdiconsCleanIcons', dependsOn: cloneRepositoryTask)
        def copyIconsTask = project.task('mdiconsCopyIcons', dependsOn: [cloneRepositoryTask, cleanIconsTask])
        def mainTask = project.task('mdicons', dependsOn: [copyIconsTask, cleanIconsTask, cloneRepositoryTask])

        project.plugins.withType(AppPlugin) {
            project.tasks.findByName("preBuild").dependsOn(mainTask)
        }
        project.plugins.withType(LibraryPlugin) {
            project.tasks.findByName("preBuild").dependsOn(mainTask)
        }

        project.afterEvaluate {
            def configChanged = project.mdicons.isChanged(project)
            def cacheDir = new File(project.mdicons.cachePath);
            def pattern = project.mdicons.pattern
            def resourceDir = project.file(project.mdicons.resourcePath)

            def iconTypes = [
                'action','alert','av','communication','content','device',
                'editor','file','hardware','image','maps','navigation',
                'notification','social','toggle'
            ] as Set

            if (!cacheDir.isDirectory()) {
                def repoUrl = 'git@github.com:google/material-design-icons.git'

                cloneRepositoryTask.doLast {
                    def command = "git clone ${repoUrl} ${cacheDir.getAbsolutePath()}"
                    command.execute().waitFor()
                }
            }

            if (configChanged && cacheDir.isDirectory()) {
                cleanIconsTask.doLast {
                    eachIconFiles(cacheDir, iconTypes, null) { cachedIconFile ->
                        def projectTypedDrawableDir = new File(resourceDir, cachedIconFile.getParentFile().getName())

                        def projectResourceFile = new File(projectTypedDrawableDir, cachedIconFile.getName())
                        if (projectResourceFile.exists()) {
                            projectResourceFile.delete()
                        }
                    }
                }
            }

            if (configChanged && cacheDir.isDirectory() && pattern != null) {
                copyIconsTask.doLast {
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
            }

            mainTask.doLast {
                project.mdicons.save(project)
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

