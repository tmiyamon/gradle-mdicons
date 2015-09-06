package com.tmiyamon.mdicons

import com.google.gson.Gson
import org.gradle.api.Project
import org.gradle.configuration.BuildConfigurer

class ColorSet {
    List<ColorGroup> colorGroups = []

    static ColorSet populate(Project project) {
        def colorSet = new ColorSet()
        def loader = new ColorLoader()

        def androidProject = AndroidProject.build(project)

        [
            loader.loadPreset(),
            loader.loadUserColorsInAndroidProject(androidProject),
            loader.loadUserColorsInGradle(project)
        ].each { colorGroup ->
            if (!colorGroup.isEmpty()) {
                colorSet.colorGroups << colorGroup
            }
        }

        colorSet
    }

    Map<String, ColorHex> colorIndex() {
        def index = [:]

        colorGroups.each { _, colors ->
            colors.each { name, hex ->
                index[name] = hex
            }
        }

        index
    }

    String toPrettyString() {
        def maxKeyNameLength = colorGroups.collect { it.colors.keySet().toList() }.flatten()*.length().max()
        def buffer = new StringBuffer()
        def newline = System.getProperty("line.separator")

        colorGroups.each { colorGroup ->
            buffer.append(colorGroup.name)
            buffer.append(newline)

            colorGroup.colors.each { name, colorHex ->
                buffer.append("  $name${" "*(maxKeyNameLength - name.length())}  $colorHex.hex")
                buffer.append(newline)
            }
            buffer.append(newline)
        }

        buffer.toString()
    }

    static class ColorGroup {
        String name
        Map<String, ColorHex> colors

        ColorGroup(String name, Map<String, ColorHex> colors) {
            this.name = name
            this.colors = colors
        }

        boolean isEmpty() {
            colors.isEmpty()
        }
    }

    static class ColorLoader {
        static final PRESET_FILE_NAME = "com/tmiyamon/mdicons/colors.json"

        static ColorGroup loadUserColorsInAndroidProject(AndroidProject androidProject) {
            def colors = new LinkedHashMap<String, String>()
            def colorFile = Utils.file(androidProject.project.rootDir, "src", "main", "res", "values", "colors.xml")

            if (colorFile.isFile()) {
                def resources = new XmlSlurper().parse(colorFile)

                resources.color.each {
                    colors[it.@name.text()] = it.text()
                }
            }

            new ColorGroup("User Colors ($colorFile)", utilize(colors))
        }

        static ColorGroup loadPreset() {
            def json = ColorLoader.class.getClassLoader().getResource(PRESET_FILE_NAME).text
            new ColorGroup("Material Design Colors", utilize(new Gson().fromJson(json, new LinkedHashMap<String, String>().getClass()) as Map<String, String>))
        }

        static ColorGroup loadUserColorsInGradle(Project project) {
            new ColorGroup("User Colors (${Utils.file(project.rootDir, "build.gradle").absolutePath})", utilize(project.mdicons.defcolors as Map<String, String>))
        }

        static Map<String, ColorHex> utilize(Map<String, String> colors) {
            def converted = new LinkedHashMap<String, ColorHex>()

            colors.each { name, hex ->
                converted[name] = new ColorHex(hex)
            }

            converted
        }
    }
}
