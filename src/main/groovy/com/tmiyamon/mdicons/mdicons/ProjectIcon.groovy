package com.tmiyamon.mdicons.mdicons

import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/30/15.
 */
class ProjectIcon {
    String density
    String fileName
    Project project

    def toFile() {
        new File(project.file("src/main/res/drawable-${density}"), fileName)
    }

}
