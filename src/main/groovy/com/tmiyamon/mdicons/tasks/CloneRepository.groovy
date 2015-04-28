package com.tmiyamon.mdicons.tasks

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.MaterialDesignIconsRepository
import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
public class CloneRepository extends AbstractTaskWrapper {
    def repository = new MaterialDesignIconsRepository()

    CloneRepository(Project project) {
        super(project)
    }

    @Override
    def doOnAfterEvaluate(Evaluator evaluator) {
        this.task.outputs.dir evaluator.cacheDir
        this.task.doLast {
            println 'A message which is logged at INFO level'
            repository.cloneTo evaluator.cacheDir
        }
    }
}
