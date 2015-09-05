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

    void setDensities(def value) {
        densities = normalize("densities", value)
    }

    void setNames(def value) {
        names = normalize("names", value)
    }

    void setColors(def value) {
        colors = normalize("colors", value)
    }

    void setSizes(def value) {
        sizes = normalize("sizes", value)
    }

    static def normalize(def property, def value) {
        if (value instanceof List<String>) {
            value
        } else if (value instanceof String) {
            [value]
        } else {
            throw new IllegalArgumentException("$property accepts only List<String> or String but got $value")
        }
    }
}
