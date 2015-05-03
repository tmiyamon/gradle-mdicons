package com.tmiyamon.mdicons.mdicons

import groovy.json.JsonSlurper

/**
 * Created by tmiyamon on 2/11/15.
 */
class MaterialColor {
    static final FILE_NAME = "com/tmiyamon/colors.json"
    static final MaterialColor instance = new MaterialColor()

    Map<String, String> colors
    private MaterialColor() {
        getClass().classLoader.getResourceAsStream(FILE_NAME).withStream {
            colors = new JsonSlurper().parse(it as InputStream) as Map<String, String>
        }
    }

    def getHexFrom(String color) {
        def colorHex = colors[color]
        if (colorHex == null) {
            throw new IllegalArgumentException("Color not supported: ${color}. See the list in https://github.com/tmiyamon/gradle-mdicons/blob/master/src/main/resources/com/tmiyamon/colors.json")
        }
        return colorHex
    }
}
