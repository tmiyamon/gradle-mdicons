package com.tmiyamon.mdicons.mdicons

import org.gradle.api.Project

/**
 * Created by tmiyamon on 4/26/15.
 */
class Evaluator {

    File cacheDir
    File resourceDir
    boolean configChanged
    String pattern
    List<Map<String, String>> groups
    def copiedFilePaths

    public Evaluator(Project project) {
        MaterialDesignIconsPluginExtension ext = project.mdicons
        cacheDir = new File(ext.cachePath);
        resourceDir = project.file(ext.resourcePath)
        configChanged = ext.isChanged(project)
        pattern = ext.buildPattern()
        groups = ext.groups
        //TODO
        copiedFilePaths = []
    }
}
