package com.tmiyamon

import org.gradle.api.Project

/**
 * Created by tmiyamon on 12/24/14.
 */
public class MaterialDesignIconsPluginExtension {
    static final String FILENAME = '.mdicons'
    static final def SAVE_FIELD_NAMES = ['cachePath', 'resourcePath', 'pattern']

    String cachePath = "${System.getProperty("user.home")}/.material_design_icons"
    String resourcePath = 'src/main/res'
    String pattern

    public MaterialDesignIconsPluginExtension() {}

    public void save(Project project) {
        def that = this
        project.file(FILENAME).withWriter { out ->
            SAVE_FIELD_NAMES.each { field ->
                out.writeLine("${field}=${Utils.getValue(that, field)}")
            }
        }
    }

    public boolean isChanged(Project project) {
        def prevConfig = project.file(FILENAME)

        def fields = SAVE_FIELD_NAMES as Set
        def that = this

        if (prevConfig.exists()) {
            boolean result = true
            prevConfig.splitEachLine("=") { String field, def value ->
                if (fields.contains(field)) {
                    result = result && (value == Utils.getValue(that, field))
                }
            }
            return !result
        } else {
            return true
        }

    }

    @Override
    public String toString() {
        return "MaterialDesignIconsPluginExtension: pattern=${pattern}, cachePath=${cachePath}, resourcePath=${resourcePath}"
    }
}
