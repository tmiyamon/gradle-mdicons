package com.tmiyamon.mdicons

import org.gradle.api.Project

class MaterialDesignColor extends HashMap<String, String> {

    List<Integer> rgb(String name) {
        def i = Integer.decode(this.get(name)).intValue()
        [(i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF]
    }

    static MaterialDesignColor populate(Project project) {
        def color = new MaterialDesignColor()

        color << MaterialColorLoader.load()
        color << project.mdicons.defcolors

        color
    }
}
