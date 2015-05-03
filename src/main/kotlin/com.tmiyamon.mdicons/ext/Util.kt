package com.tmiyamon.mdicons.ext

import com.tmiyamon.mdicons
import com.tmiyamon.mdicons.Extension
import org.gradle.api.Project

fun getExtensionOf(project: Project): mdicons.Extension {
    return project.getExtensions().getByType(javaClass<Extension>())
}

