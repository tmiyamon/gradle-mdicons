package com.tmiyamon.mdicons

import org.gradle.api.Project

class AndroidProject {
    static final String RES_RELATIVE_PATH = Utils.pathjoin("src", "main", "res")
    static final String RES_MDICONS_RELATIVE_PATH = Utils.pathjoin("src", "main", "res-${MaterialDesignIconsPlugin.NAME}")

    final Project project
    final File resDir
    final File resMdiconsDir

    static AndroidProject build(Project project) {
        new AndroidProject(project)
    }

    protected AndroidProject(Project project) {
        this.project = project
        this.resDir = Utils.file(project.projectDir, RES_RELATIVE_PATH)
        this.resMdiconsDir = Utils.file(project.projectDir, RES_MDICONS_RELATIVE_PATH)

    }

    File resMdiconsDirOf(String density) {
        Utils.file(resMdiconsDir, "${project.mdicons.iconDirPrefix}-${density}")
    }

    def copy(Closure c) {
        project.copy(c)
    }
}
