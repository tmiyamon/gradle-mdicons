package com.tmiyamon.mdicons

import org.gradle.api.Project

class AndroidProject {
    final Project project
    final File resDir

    static AndroidProject newWithGradleProject(Project project) {
        new AndroidProject(project)
    }

    protected AndroidProject(Project project) {
        this.project = project
        this.resDir = Utils.file(project.rootDir, "src", "main", "res")
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
