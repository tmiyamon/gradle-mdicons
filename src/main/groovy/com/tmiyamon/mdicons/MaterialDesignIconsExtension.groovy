package com.tmiyamon.mdicons

import org.gradle.api.NamedDomainObjectContainer

class MaterialDesignIconsExtension {
    final private NamedDomainObjectContainer<AssetTarget> userAssets

    String iconDirPrefix = "mipmap"
    List<String> defaultDensities = MaterialDesignIconsRepository.DENSITIES
    Map<String, String> defcolors = [:]

    public MaterialDesignIconsExtension(NamedDomainObjectContainer<AssetTarget> assets) {
        this.userAssets = assets
    }

    def assets(Closure c) {
        this.userAssets.configure(c)
    }

    def defcolor(String name, String hex) {
        this.defcolors[name] = hex
    }
}
