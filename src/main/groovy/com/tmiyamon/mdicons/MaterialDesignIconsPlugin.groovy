package com.tmiyamon.mdicons

import com.tmiyamon.mdicons.task.CopyAsset
import org.gradle.api.Plugin
import org.gradle.api.Project

class MaterialDesignIconsPlugin implements Plugin<Project> {
    static final String NAME = "mdicons"
    static final String GROUP = "MaterialDesignIcons"

    @Override
    void apply(Project project) {
        def assets = project.container(AssetTarget) {
            String assetName = it.toString()

            project.task(
                type: CopyAsset,
                group: GROUP,
                description: "Copy icons defind in assets ${assetName}",
                "copy${Utils.pascalize(assetName)}"
            ) {
                it.assetName = assetName
            }

            project.extensions.create(it, AssetTarget, assetName)
        }

        def mdicons = new MaterialDesignIconsExtension(assets)
        project.convention.plugins.mdicons = mdicons
        project.extensions.mdicons = mdicons

    }
}
