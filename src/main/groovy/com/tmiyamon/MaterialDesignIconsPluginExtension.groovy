package com.tmiyamon

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

    public MaterialDesignIconsPluginExtension() {}

    public Map<String, Serializable> toMap() {
        return [
            cachePath: cachePath,
            resourcePath: resourcePath,
            patterns: patterns
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
            return toMap() != new JsonSlurper().parse(prevConfig)
        }
        return true
    }

    public void pattern(String str) {
        if (Utils.isNotEmpty(str)) {
            patterns.add(str)
        }
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
                '}';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        MaterialDesignIconsPluginExtension that = (MaterialDesignIconsPluginExtension) o

        if (cachePath != that.cachePath) return false
        if (patterns.size() != that.patterns.size() || (patterns - that.patterns).size() != 0)  return false
        if (resourcePath != that.resourcePath) return false

        return true
    }

    int hashCode() {
        int result
        result = (cachePath != null ? cachePath.hashCode() : 0)
        result = 31 * result + (resourcePath != null ? resourcePath.hashCode() : 0)
        result = 31 * result + (patterns != null ? patterns.hashCode() : 0)
        return result
    }
}
