package com.tmiyamon

import org.gradle.api.Project

/**
 * Created by tmiyamon on 12/24/14.
 */
public class MaterialDesignIconsPluginExtension {
    static final String FILENAME = '.mdicons'

    String cachePath = "${System.getProperty("user.home")}/.material_design_icons"
    String resourcePath = 'src/main/res'
    def patterns = [];

    public MaterialDesignIconsPluginExtension() {}

    public Map<String, String> toMap() {
        def that = this
        return new LinkedHashMap<String, String>(){{
            put('cachePath', that.cachePath)
            put('resourcePath', that.resourcePath)
            if (that.patterns != null) {
                put('patterns', that.patterns.join(','))
            }
        }}
    }

    public static MaterialDesignIconsPluginExtension fromMap(Map<String, String> map) {
        def ext = new MaterialDesignIconsPluginExtension()
        ext.cachePath = map['cachePath']
        ext.resourcePath = map['resourcePath']

        def patterns = map['patterns']
        if (Utils.isNotEmpty(patterns)) {
            patterns.split(',').each { pattern ->
                ext.pattern pattern
            }
        }
        return ext
    }

    public void save(Project project) {
        project.file(FILENAME).withWriter { out ->
            toMap().each { key, value ->
                out.writeLine("${key}=${value}")
            }
        }
    }

    public boolean isChanged(Project project) {
        def prevConfig = project.file(FILENAME)
        def that = this

        if (prevConfig.exists()) {
            def map = new HashMap<String, String>()
            prevConfig.splitEachLine("=") {
                map.put(it[0], it[1])
            }
            return !that.toMap().equals(map)
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
