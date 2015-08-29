package com.tmiyamon.mdicons

import org.gradle.api.NamedDomainObjectContainer

class MaterialDesignIconsExtension {
    final private NamedDomainObjectContainer<AssetTarget> userAssets

    public MaterialDesignIconsExtension(NamedDomainObjectContainer<AssetTarget> assets) {
        this.userAssets = assets
    }

    def assets(Closure c) {
        this.userAssets.configure(c)
    }
}
