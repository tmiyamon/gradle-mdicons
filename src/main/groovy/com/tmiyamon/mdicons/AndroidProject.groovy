package com.tmiyamon.mdicons

import org.gradle.api.Project

class AndroidProject {
    static final String RES_RELATIVE_PATH = Utils.pathjoin("src", "main", "res-${MaterialDesignIconsPlugin.NAME}")
    final Project project
    final File resDir

    static AndroidProject newWithGradleProject(Project project) {
        new AndroidProject(project)
    }

    protected AndroidProject(Project project) {
        this.project = project
        this.resDir = Utils.file(project.rootDir, RES_RELATIVE_PATH)
    }

    List<File> iconDirsOf(List<String> densities) {
        densities.collect { iconDirOf(it) }
    }

    File iconDirOf(String density) {
        new File(resDir, "${project.mdicons.iconDirPrefix}-${density}")
    }

    def copy(Closure c) {
        project.copy(c)
    }
}
