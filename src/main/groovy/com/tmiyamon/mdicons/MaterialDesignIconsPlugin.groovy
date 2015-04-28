package com.tmiyamon.mdicons

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.tmiyamon.mdicons.tasks.CleanIcons
import com.tmiyamon.mdicons.tasks.CloneRepository
import com.tmiyamon.mdicons.tasks.CopyIconsByGroup
import com.tmiyamon.mdicons.tasks.CopyIconsByPattern
import com.tmiyamon.mdicons.tasks.SaveConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by tmiyamon on 12/24/14.
 */

public class MaterialDesignIconsPlugin implements Plugin<Project> {
    public static final String NAME = "mdicons"

    @Override
    void apply(Project project) {
        project.extensions.create(NAME, MaterialDesignIconsPluginExtension)

        def cloneRepository    = new CloneRepository(project)
        def cleanIcons         = new CleanIcons(project).dependsOn(cloneRepository)
        def copyIconsByPattern = new CopyIconsByPattern(project).dependsOn(cloneRepository, cleanIcons)
        def copyIconsByGroup   = new CopyIconsByGroup(project).dependsOn(cloneRepository, cleanIcons)
        def saveConfig         = new SaveConfig(project).dependsOn(copyIconsByPattern, copyIconsByGroup, cleanIcons, cloneRepository)

        def tasks = [cloneRepository, cleanIcons, copyIconsByPattern, copyIconsByGroup, saveConfig]

        project.plugins.withType(AppPlugin) {
            project.tasks.findByName("preBuild").dependsOn(saveConfig.task)
        }
        project.plugins.withType(LibraryPlugin) {
            project.tasks.findByName("preBuild").dependsOn(saveConfig.task)
        }

        project.afterEvaluate {
            Evaluator evaluator = new Evaluator(project)
            tasks.each { task ->
                task.doOnAfterEvaluate(evaluator)
            }
        }
    }

//        def cloneRepositoryTask = project.task('mdiconsCloneRepository')
//        def cleanIconsTask = project.task('mdiconsCleanIcons', dependsOn: cloneRepositoryTask)
//        def copyIconsByPatternsTask = project.task('mdiconsCopyIconsByPatterns', dependsOn: [cloneRepositoryTask, cleanIconsTask])
//        def copyIconsByGroupsTask = project.task('mdiconsCopyIconsByGroups', dependsOn: [cloneRepositoryTask, cleanIconsTask])
//        def mainTask = project.task('mdicons', dependsOn: [copyIconsByPatternsTask, copyIconsByGroupsTask, cleanIconsTask, cloneRepositoryTask])
//
//
//
//        project.plugins.withType(AppPlugin) {
//            project.tasks.findByName("preBuild").dependsOn(mainTask)
//        }
//        project.plugins.withType(LibraryPlugin) {
//            project.tasks.findByName("preBuild").dependsOn(mainTask)
//        }
//
//        project.afterEvaluate {
//            def configChanged = project.mdicons.isChanged(project)
//            def cacheDir = new File(project.mdicons.cachePath);
//            def pattern = project.mdicons.buildPattern()
//            def resourceDir = project.file(project.mdicons.resourcePath)
//
//            if (!cacheDir.isDirectory()) {
//                def repoUrl = 'git@github.com:google/material-design-icons.git'
//
//                cloneRepositoryTask.doLast {
//                    def command = "git clone ${repoUrl} ${cacheDir.getAbsolutePath()}"
//                    command.execute().waitFor()
//                }
//            }
//
//            if (configChanged && cacheDir.isDirectory()) {
//                cleanIconsTask.doLast {
//                    Map<String, List<Icon>> iconMapping = [:].withDefault {[]}
//                    Icon.eachProjectResourceIcons(resourceDir) { Icon icon ->
//                        iconMapping[icon.newCanonical().fileName] << icon
//                    }
//                    Icon.eachCacheCanonicalIcons(cacheDir) { Icon icon ->
//                        iconMapping[icon.fileName].each {
//                            it.getProjectResourceVariantFiles(resourceDir).each { it.delete() }
//                        }
//                    }
//                }
//            }
//
//            if (configChanged && cacheDir.isDirectory() && Utils.isNotEmpty(pattern)) {
//                copyIconsByPatternsTask.doLast {
//                    eachIconFiles(cacheDir, Icon.CATEGORIES, pattern) { cachedIconFile ->
//                        def projectTypedDrawableDir = new File(resourceDir, cachedIconFile.getParentFile().getName())
//                        if (!projectTypedDrawableDir.isDirectory()) {
//                            projectTypedDrawableDir.mkdir()
//                        }
//
//                        if (!new File(projectTypedDrawableDir, cachedIconFile.getName()).exists()) {
//                            project.copy {
//                                from cachedIconFile
//                                into projectTypedDrawableDir
//                            }
//                        }
//                    }
//                }
//            }
//
//            List<Map<String, String>> groups = project.mdicons.groups
//            if (configChanged && cacheDir.isDirectory() && Utils.isNotEmpty(groups)) {
//                copyIconsByGroupsTask.doLast {
//                    def iconGroups = groups.collect { new IconGroup(it) }
//
//                    Icon.eachCacheCanonicalIconsMatchedToGroups(cacheDir, iconGroups) { Icon canonicalIcon ->
//                        iconGroups.each { IconGroup iconGroup ->
//                            if (canonicalIcon.fileName =~ iconGroup.canonicalPattern) {
//                                canonicalIcon.variants.each {
//                                    def tintIcon = it.newWithColor(iconGroup.color)
//                                    if (!tintIcon.isCacheExists(cacheDir)) {
//                                        tintIcon.generateCache(cacheDir)
//                                    }
//
//                                    project.copy {
//                                        from tintIcon.getCacheFile(cacheDir)
//                                        into tintIcon.getProjectResourceFile(resourceDir).parentFile
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            mainTask.doLast {
//                project.mdicons.save(project)
//            }
//        }
//    }
//
//    /**
//     * Traverse material design icons repository.
//     * The structure is expected as below
//     *
//     * /iconType/drawable-x/ic_x.png
//     *
//     * @param root
//     * @param iconTypes
//     * @param pattern
//     * @param closure
//     * @return
//     */
//    def eachIconFiles(File root, Set iconTypes, String pattern, Closure closure) {
//        root.eachDirMatch({ iconTypes.contains(new File(it).getName()) }) {
//            it.eachDirMatch({ new File(it).getName().startsWith("drawable")}) {
//                if (Utils.isNotEmpty(pattern)) {
//                    it.eachFileMatch({ new File(it).getName() =~ pattern }, closure)
//                } else {
//                    it.eachFile(closure)
//                }
//            }
//        }
//    }

}

