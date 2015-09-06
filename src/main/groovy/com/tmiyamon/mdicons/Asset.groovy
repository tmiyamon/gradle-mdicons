package com.tmiyamon.mdicons

import org.gradle.api.Project

class Asset {
    final String name
    final IconSpec iconSpec

    static Asset newWithAssetTarget(Project project, AssetTarget target) {
        new Asset(target.name, IconSpec.newWithAssetTarget(project, target))
    }

    static Asset findOf(Project project, String assetName) {
        def assetTargets = project.userAssets as List<AssetTarget>
        newWithAssetTarget(project, assetTargets.find { it.name == assetName })
    }

    static List<Asset> allOf(Project project) {
        def assetTargets = project.userAssets as List<AssetTarget>
        assetTargets.collect { newWithAssetTarget(project, it) }
    }

    Asset(String name, IconSpec iconSpec) {
        this.name = name
        this.iconSpec = iconSpec
    }

    static class IconSpec {
        final List<String> densities
        final List<String> names
        final List<String> colors
        final List<String> sizes

        IconSpec(List<String> densities, List<String> names, List<String> colors, List<String> sizes) {
            this.densities = densities
            this.names = names
            this.colors = colors
            this.sizes = sizes
        }

        static IconSpec newWithAssetTarget(Project project, AssetTarget target) {
            validateRequired(target, "names", "colors", "sizes")

            new IconSpec(
                target.densities ?: project.mdicons.defaultDensities,
                target.names,
                target.colors,
                target.sizes
            )
        }

        static def validateRequired(AssetTarget target, String...fields) {
            fields.each { field ->
                if (target."$field" == null) {
                    throw new IllegalArgumentException("\"$field\" is required in \"$target.name\"")
                }
            }
        }
    }
}
