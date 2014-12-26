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
                out.writeLine("${field}=${that."${Utils.getGetterName(field)}"()}")
            }
        }
    }

    public static MaterialDesignIconsPluginExtension readFrom(Project project) {
        def prevInfo = new MaterialDesignIconsPluginExtension()
        def saveFields = SAVE_FIELD_NAMES as Set

        def configFile = project.file(FILENAME)

        if (configFile.exists()) {
            configFile.splitEachLine('=') { key, value ->
                if (saveFields.contains(key)) {
                    prevInfo."${Utils.getSetterName(key)}"(value)
                }
            }
        }
        return prevInfo
    }

    public boolean isEqualTo(MaterialDesignIconsPluginExtension prev) {
        def that = this
        return SAVE_FIELD_NAMES.inject (true) { acc, field ->
            acc && (Utils.getValue(prev, field) == Utils.getValue(that, field))
        }
    }

    public boolean isConfigChanged(Project project) {
        return !isEqualTo(MaterialDesignIconsPluginExtension.readFrom(project))
    }

}
