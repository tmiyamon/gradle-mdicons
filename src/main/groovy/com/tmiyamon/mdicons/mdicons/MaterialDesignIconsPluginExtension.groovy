package com.tmiyamon.mdicons.mdicons

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gradle.api.Project

/**
 * Created by tmiyamon on 12/24/14.
 */
public class MaterialDesignIconsPluginExtension {
    static final String FILENAME = '.mdicons'

    String cachePath = "${System.getProperty("user.home")}/.material_design_icons"
    String resourcePath = 'src/main/res'
    private def patterns = [];
    private def groups = []

    public MaterialDesignIconsPluginExtension() {}

    public Map<String, Serializable> toMap() {
        return [
            cachePath: cachePath,
            resourcePath: resourcePath,
            patterns: patterns,
            groups: groups
        ]
    }

    public String toJson() {
        return new JsonBuilder(toMap()).toString()
    }

    public void save(Project project) {
        project.file(FILENAME).withWriter { it.writeLine(toJson()) }
    }

    public boolean isChanged(Project project) {
        File prevConfig = project.file(FILENAME)

        if (prevConfig.exists()) {
            try {
                return toMap() != new JsonSlurper().parse(prevConfig)
            } catch (Exception e) {
                project.logger.warn("Ignore previous configuration for mdicons due to its corruption.")
                return true
            }
        }
        return true
    }

    public void pattern(String str) {
        if (Utils.isNotEmpty(str)) {
            patterns.add(str)
        }
    }

    public void group(Map<String, String> args) {
        def required = args.subMap('name', 'color', 'size')
        if (Utils.isNotEmpty(required['name']) &&
                Utils.isNotEmpty(required['color']) &&
                Utils.isNotEmpty(required['size'])) {

            groups.add(required)
        }
    }

    def getGroups() {
        return groups
    }

    def getPatterns() {
        return patterns
    }

    public String buildPattern() {
        if (Utils.isNotEmpty(patterns)) {
            return "(${patterns.join("|")})"
        }
        return null
    }

    @Override
    public String toString() {
        return "MaterialDesignIconsPluginExtension{" +
                "cachePath='" + cachePath + '\'' +
                ", resourcePath='" + resourcePath + '\'' +
                ", patterns=" + patterns +
                ", groups=" + groups +
                '}';
    }
}
