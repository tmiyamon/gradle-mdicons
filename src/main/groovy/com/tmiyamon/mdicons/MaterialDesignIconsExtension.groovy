package com.tmiyamon.mdicons

import org.gradle.api.NamedDomainObjectContainer

class MaterialDesignIconsExtension {
    final private NamedDomainObjectContainer<AssetTarget> userAssets

    String iconDirPrefix = "mipmap"
    Map<String, String> mdcolor = MaterialColorLoader.load()
    List<String> defaultDensities = MaterialDesignIcons.DENSITIES

    public MaterialDesignIconsExtension(NamedDomainObjectContainer<AssetTarget> assets) {
        this.userAssets = assets
    }

    def assets(Closure c) {
        this.userAssets.configure(c)
    }
}
