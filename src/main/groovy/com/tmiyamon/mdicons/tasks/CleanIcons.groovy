package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.Icon
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
public class CleanIcons extends AbstractTaskWrapper {
    CleanIcons(Project project) {
        super(project)
    }

    def doOnAfterEvaluate(Evaluator evaluator) {
        if (evaluator.configChanged && evaluator.cacheDir.isDirectory()) {
            Map<String, List<Icon>> iconMapping = [:].withDefault {[]}
            Icon.eachProjectResourceIcons(evaluator.resourceDir) { Icon icon ->
                iconMapping[icon.newCanonical().fileName] << icon
            }
            Icon.eachCacheCanonicalIcons(evaluator.cacheDir) { Icon icon ->
                iconMapping[icon.fileName].each {
                    it.getProjectResourceVariantFiles(evaluator.resourceDir).each { it.delete() }
                }
            }
        }
    }
}
