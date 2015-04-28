package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.Icon
import com.tmiyamon.mdicons.Utils
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
public class CopyIconsByPattern extends AbstractTaskWrapper {
    CopyIconsByPattern(Project project) {
        super(project)
    }

    def doOnAfterEvaluate(Evaluator evaluator) {
        if (evaluator.configChanged &&
                evaluator.cacheDir.isDirectory() &&
                Utils.isNotEmpty(evaluator.pattern)) {

            this.task.doLast {

                Utils.eachIconFiles(evaluator.cacheDir, Icon.CATEGORIES, evaluator.pattern) { cachedIconFile ->
                    def projectTypedDrawableDir = new File(evaluator.resourceDir, cachedIconFile.parentFile.name)
                    if (!projectTypedDrawableDir.isDirectory()) {
                        projectTypedDrawableDir.mkdir()
                    }

                    if (!new File(projectTypedDrawableDir, cachedIconFile.name).exists()) {
                        project.copy {
                            from cachedIconFile
                            into projectTypedDrawableDir
                        }
                    }
                }
            }
        }
    }
}
