package com.tmiyamon.mdicons.ext

import com.tmiyamon.mdicons
import com.tmiyamon.mdicons.Extension
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.FileFilter

fun getExtensionOf(project: Project): mdicons.Extension {
    return project.getExtensions().getByType(javaClass<Extension>())
}

fun <T: Task> Project.taskOf(taskClass: Class<T>): Task {
    return task(mapOf(
            "type" to taskClass,
            "group" to "MaterialDesignIcons"
    ), "${MaterialDesignIconsPlugin.NAME}${taskClass.getSimpleName()}")
}

fun compose<A, B, C>(f: (B) -> C, g: (A) -> B): (A) -> C {
    return { x -> f(g(x)) }
}

fun composeFileFilter(vararg filters: FileFilter): FileFilter {
    return FileFilter { file ->
        filters.fold(true) { acc, f -> acc && f.accept(file) }
    }
}

fun trace(s: Any) {
    MaterialDesignIconsPlugin.logger.trace(s.toString())
}
fun debug(s: Any) {
    MaterialDesignIconsPlugin.logger.debug(s.toString())
}
fun info(s: Any) {
    MaterialDesignIconsPlugin.logger.info(s.toString())
}
fun warn(s: Any) {
    MaterialDesignIconsPlugin.logger.warn(s.toString())
}
fun error(s: Any) {
    MaterialDesignIconsPlugin.logger.error(s.toString())
}

