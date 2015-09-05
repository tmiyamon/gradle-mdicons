package com.tmiyamon.mdicons

import org.gradle.api.Named
import org.gradle.api.internal.project.ProjectInternal

import javax.rmi.CORBA.Util

class AssetTarget implements Named {
    String name
    ProjectInternal target

    List<String> densities
    List<String> names
    List<String> colors
    List<String> sizes

    public AssetTarget(String name) {
        super()
        this.name = name
        this.target = target
    }

    def colors(String...values) {
        colors = values
    }

    def names(String...values) {
        names = values
    }

    def sizes(String...values) {
        sizes = values
    }

    def densities(String... values) {
        densities = values
    }

}
