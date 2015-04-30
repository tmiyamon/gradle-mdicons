package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.MaterialDesignIconsRepository
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
public class CloneRepository extends AbstractTaskWrapper {
    CloneRepository(Project project) {
        super(project)
    }

    @Override
    def doOnAfterEvaluate(Evaluator evaluator) {
        def repository = new MaterialDesignIconsRepository(evaluator.cacheDir)

        this.task.outputs.dir evaluator.cacheDir
        this.task.doLast {
            repository.clone()
        }
    }
}
