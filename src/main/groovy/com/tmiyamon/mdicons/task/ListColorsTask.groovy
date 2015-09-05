package com.tmiyamon.mdicons.task

import com.tmiyamon.mdicons.MaterialColorLoader
import com.tmiyamon.mdicons.MaterialDesignIconsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class ListColorsTask extends DefaultTask {
    @TaskAction
    def run() {
        def mdcolors = MaterialColorLoader.load()
        def defcolors = project.mdicons.defcolors as Map<String, String>

        def maxNameLength = (mdcolors.keySet() + defcolors.keySet())*.length().max()

        printColors(mdcolors, "Material Design Colors", maxNameLength)
        if (!defcolors.isEmpty()) {
            printColors(defcolors, "User Colors", maxNameLength)
        }
    }

    static def printColors(Map<String, String> colors, String title, int maxNameLength) {
        println title
        colors.each { name, hex ->
            println "  $name${" "*(maxNameLength - name.length() + 2)}$hex"
        }
        println ""
    }

    static def createTask(Project project) {
        project.task(
            type: ListColorsTask,
            group: MaterialDesignIconsPlugin.GROUP,
            description: "List color set",
            "listColors"
        )
    }
}
