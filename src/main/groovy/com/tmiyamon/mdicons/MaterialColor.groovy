package com.tmiyamon.mdicons

import groovy.json.JsonSlurper

/**
 * Created by tmiyamon on 2/11/15.
 */
class MaterialColor {
    static final FILE_NAME = "com/tmiyamon/colors.json"
    static final MaterialColor instance = new MaterialColor()

    def colors
    private MaterialColor() {
        getClass().classLoader.getResourceAsStream(FILE_NAME).withStream {
            colors = new JsonSlurper().parse(it)
        }
    }

    def getHexFrom(String name) {
        colors[name]
    }
}
