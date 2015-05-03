package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.Evaluator
import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.Result
import com.tmiyamon.mdicons.ext.getExtensionOf
import com.tmiyamon.mdicons.repository.MaterialDesignIcons
import org.gradle.api.Task
import java.io.File
import java.io.FileFilter
import java.util.regex.Pattern

class CopyIconsByPattern(val task: Task) : TaskLike {
    override fun doOnAfterEvaluate(evaluator: Evaluator) {
        task.doLast {
            val ext = getExtensionOf(task.getProject())
            val repository = MaterialDesignIcons(File(Extension.CACHE_PATH))

            val results = repository.copyIconFileMatch(
                    task.getProject(),
                    FileFilter { it.name.matches(ext.buildPattern()) })


            ext.results.addAll(results.map {
                Result(
                    it.getSourceRelativePath(),
                    it.getDestinationRelativePath(),
                    task.getName())
            })
        }
    }
}
