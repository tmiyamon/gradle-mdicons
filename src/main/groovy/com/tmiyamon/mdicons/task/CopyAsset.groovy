package com.tmiyamon.mdicons.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class CopyAsset extends DefaultTask {
    String assetName

    @TaskAction
    def run() {
        def assets = project.userAssets
        println(assets.find { it.name == assetName })
    }
}
