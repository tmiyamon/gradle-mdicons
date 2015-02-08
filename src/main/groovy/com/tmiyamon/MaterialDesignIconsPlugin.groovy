package com.tmiyamon

import bsh.StringUtil
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import groovy.io.FileType
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
        def copyIconsByPatternsTask = project.task('mdiconsCopyIconsByPatterns', dependsOn: [cloneRepositoryTask, cleanIconsTask])
        def copyIconsByGroupsTask = project.task('mdiconsCopyIconsByGroups', dependsOn: [cloneRepositoryTask, cleanIconsTask])
        def mainTask = project.task('mdicons', dependsOn: [copyIconsByPatternsTask, copyIconsByGroupsTask, cleanIconsTask, cloneRepositoryTask])

        project.plugins.withType(AppPlugin) {
            project.tasks.findByName("preBuild").dependsOn(mainTask)
        }
        project.plugins.withType(LibraryPlugin) {
            project.tasks.findByName("preBuild").dependsOn(mainTask)
        }

        project.afterEvaluate {
            def configChanged = project.mdicons.isChanged(project)
            def cacheDir = new File(project.mdicons.cachePath);
            def pattern = project.mdicons.buildPattern()
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

            if (configChanged && cacheDir.isDirectory() && Utils.isNotEmpty(pattern)) {
                copyIconsByPatternsTask.doLast {
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

            def groups = project.mdicons.groups as List
            if (configChanged && cacheDir.isDirectory() && Utils.isNotEmpty(groups)) {
                copyIconsByGroupsTask.doLast {
                    def predefinedColor = ['white', 'black', 'grey600'] as Set<String>
                    def searchPattern = groups.collect({ Map group -> ".*(${group['name']}).*_white_${group['size']}\\.png" }).join('|')
                    project.logger.debug("[mdicons:$name] searchPattern for groups: ${searchPattern}")

                    eachIconFiles(cacheDir) { Map<String, String> iconData ->
                        def iconType = iconData['type']
                        def iconName = iconData['name']

                        if (iconName =~ searchPattern) {
                            groups.each { Map group ->
                                if (iconName =~ ".*(${group['name']}).*_white_${group['size']}") {
                                    ['drawable-mdpi',
                                     'drawable-hdpi',
                                     'drawable-xhdpi',
                                     'drawable-xxhdpi',
                                     'drawable-xxxhdpi'
                                    ].each { String density ->

                                        def matchedIconFile = iconFile(cacheDir, iconType, density, iconName)
                                        def targetIconFile = new File(matchedIconFile.absolutePath.replace('white', group['color']))

                                        if(!targetIconFile.isFile()) {
                                            def cmd = "/usr/local/bin/convert ${matchedIconFile.absolutePath} -fuzz 75% -fill ${group['color']} -opaque white -type TruecolorMatte PNG32:${targetIconFile.absolutePath}"
                                            def proc =  cmd.execute()
                                            proc.waitFor()
                                            project.logger.debug(cmd)
                                            project.logger.debug("return code: ${ proc.exitValue()}")
                                            project.logger.debug("stderr: ${proc.err.text}")
                                            project.logger.debug("stdout: ${proc.in.text}")
                                        }

                                        project.copy {
                                            from targetIconFile
                                            into new File(resourceDir, density)
                                        }
                                    }
                                }
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

    def eachIconFiles(File root, Closure closure) {
        def iconTypes = [
            'action','alert','av','communication','content','device',
            'editor','file','hardware','image','maps','navigation',
            'notification','social','toggle'
        ]
        iconTypes.each { String iconType ->
            new File(root, "${iconType}/drawable-mdpi").eachFile { File icon ->
                closure( type: iconType, name: icon.name )
            }
        }
    }

    def iconFile(File root, String iconType, String density, String iconName) {
        new File(root, "${iconType}/${density}/${iconName}")
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
                if (Utils.isNotEmpty(pattern)) {
                    it.eachFileMatch({ new File(it).getName() =~ pattern }, closure)
                } else {
                    it.eachFile(closure)
                }
            }
        }
    }

}

