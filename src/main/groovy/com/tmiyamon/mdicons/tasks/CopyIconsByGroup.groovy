package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.IconGroup
import com.tmiyamon.mdicons.MaterialDesignIconsRepository
import com.tmiyamon.mdicons.Utils
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
public class CopyIconsByGroup extends AbstractTaskWrapper {
    CopyIconsByGroup(Project project) {
        super(project)
    }

    def doOnAfterEvaluate(Evaluator evaluator) {
//        if (evaluator.configChanged &&
//            evaluator.cacheDir.isDirectory() &&
//            Utils.isNotEmpty(evaluator.groups)) {
//
//
//            this.task.doLast {
//                def iconGroups = evaluator.groups.collect { new IconGroup(it) }
//
//                MaterialDesignIconsRepository.copyIconsByGroup(project, iconGroups)
//
//                Icon.eachCacheCanonicalIconsMatchedToGroups(evaluator.cacheDir, iconGroups) { Icon canonicalIcon ->
//                    iconGroups.each { IconGroup iconGroup ->
//                        if (canonicalIcon.fileName =~ iconGroup.canonicalPattern) {
//                            canonicalIcon.variants.each {
//                                def tintIcon = it.newWithColor(iconGroup.color)
//                                if (!tintIcon.isCacheExists(evaluator.cacheDir)) {
//                                    tintIcon.generateCache(evaluator.cacheDir)
//                                }
//
//                                project.copy {
//                                    from tintIcon.getCacheFile(evaluator.cacheDir)
//                                    into tintIcon.getProjectResourceFile(evaluator.resourceDir).parentFile
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }
}
