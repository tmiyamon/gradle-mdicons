package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/27/15.
 */
public class SaveConfig extends AbstractTaskWrapper {
    SaveConfig(Project project) {
        super(project)
    }

    def doOnAfterEvaluate(Evaluator evaluator) {
//        project.mdicons.save(project)
    }
}
