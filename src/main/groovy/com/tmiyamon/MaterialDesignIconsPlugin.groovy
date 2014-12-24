package com.tmiyamon

import com.android.builder.model.AndroidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin

/**
 * Created by tmiyamon on 12/24/14.
 */

class MaterialDesignIconsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task('fetchMaterialDesignIcons') {
            def dest = "${System.getProperty("user.home")}/.material_design_icons"

            if (!new File(dest).isDirectory()) {
                def process = "git clone git@github.com:google/material-design-icons.git ${dest}".execute()
                process.waitFor()
            }
        }

        project.plugins.withType(ApplicationPlugin) {
            project.tasks.findByName('run').dependsOn('fetchMaterialDesignIcons')
        }
    }
}