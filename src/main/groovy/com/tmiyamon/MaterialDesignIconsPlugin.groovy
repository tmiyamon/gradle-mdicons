package com.tmiyamon

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tmiyamon on 12/24/14.
 */

public class MaterialDesignIconsPlugin implements Plugin<Project> {
    static def ICON_CATEGORIES = [
            'action','alert','av','communication','content','device',
            'editor','file','hardware','image','maps','navigation',
            'notification','social','toggle'
    ]
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

            def densities = [
                    'drawable-mdpi',
                    'drawable-hdpi',
                    'drawable-xhdpi',
                    'drawable-xxhdpi',
                    'drawable-xxxhdpi'
            ]

            if (!cacheDir.isDirectory()) {
                def repoUrl = 'git@github.com:google/material-design-icons.git'

                cloneRepositoryTask.doLast {
                    def command = "git clone ${repoUrl} ${cacheDir.getAbsolutePath()}"
                    command.execute().waitFor()
                }
            }

            if (configChanged && cacheDir.isDirectory()) {
                cleanIconsTask.doLast {
                    Map<String, List<IconFile>> iconMapping = [:].withDefault {[]}
                    IconFile.eachProjectResourceIcons(resourceDir) { IconFile icon ->
                        iconMapping[icon.newCanonical().fileName] << icon
                    }
                    IconFile.eachCacheCanonicalIcons(cacheDir) { IconFile icon ->
                        iconMapping[icon.fileName].each {
                            it.getProjectResourceVariantFiles(resourceDir).each { it.delete() }
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

            List<Map<String, String>> groups = project.mdicons.groups
            if (configChanged && cacheDir.isDirectory() && Utils.isNotEmpty(groups)) {
                copyIconsByGroupsTask.doLast {
                    def iconGroups = groups.collect { new IconGroup(it) }

                    IconFile.eachCacheCanonicalIconsMatchedToGroups(cacheDir, iconGroups) { IconFile canonicalIcon ->
                        iconGroups.each { IconGroup iconGroup ->
                            if (canonicalIcon.fileName =~ iconGroup.canonicalPattern) {
                                canonicalIcon.variants.each {
                                    def tintIcon = it.newWithColor(iconGroup.color)
                                    if (!tintIcon.isCacheExists(cacheDir)) {
                                        tintIcon.generateCache(cacheDir)
                                    }

                                    project.copy {
                                        from tintIcon.getCacheFile(cacheDir)
                                        into tintIcon.getProjectResourceFile(resourceDir).parentFile
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

//    def eachIconFiles(File root, Closure closure) {
//        ICON_CATEGORIES.each { String category ->
//            new File(root, "${category}/drawable-mdpi").eachFile { File iconFile ->
//                //closure( type: iconType, name: icon.name, file: icon )
//                closure(category, name, iconFile)
//            }
//        }
//    }

    def eachCanonicalIconFiles(File root, Closure closure) {
        ICON_CATEGORIES.each {
            new File(root, "${it}/drawable-mdpi").eachFile { File iconFile ->
                //closure( type: iconType, name: icon.name, file: icon )
                closure(category, name, iconFile)
            }
        }
    }

    def eachIconsAnyMatchedToGroups(File root, List<IconGroup> iconGroups, Closure closure) {
        def canonicalPatternForAllGroups = iconGroups.collect({ it.canonicalPattern }).join('|')
        eachIconFiles(root)  { String category, String name, File iconFile ->
            if (name =~ canonicalPatternForAllGroups) {
                closure(iconFile)
            }
        }

    }

    def iconFile(File root, String iconType, String density, String iconName) {
        new File(root, "${iconType}/${density}/${iconName}")
    }

    def canonicalPatternFor(Map<String, String> group) {
        ".*(${group['name']}).*_${Icon.CANONICAL_COLOR}_${group['size']}"
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

