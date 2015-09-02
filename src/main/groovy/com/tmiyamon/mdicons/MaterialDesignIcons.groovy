package com.tmiyamon.mdicons

import com.tmiyamon.mdicons.cmd.Git
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class MaterialDesignIcons {
    static final String URL = "https://github.com/google/material-design-icons.git"

    static final String ROOT_DIR_NAME = ".material_design_icons"
    static final String INDEX_FILE_NAME = ".index"
    static File DEFAULT_ROOT_DIR = new File(System.getProperty("user.home"), ROOT_DIR_NAME)

    static final List<String> CATEGORIES = [
        "action", "alert", "av", "communication", "content", "device",
        "editor", "file", "hardware", "image", "maps", "navigation",
        "notification", "social", "toggle"
    ]
    static final List<String> DENSITIES = [
        "anydpi-v21", "hdpi", "mdpi", "xhdpi", "xxhdpi", "xxxhdpi"
    ]
    static final String ICON_PATTERN = /.*(?=_(black|white)_\d+dp.png)/
    static final Git git = new Git()


    static newWithRootDir(File rootDir = DEFAULT_ROOT_DIR) {
        new MaterialDesignIcons(rootDir)
    }

    Map<String, String> createIndex() {
        def index = [:]
        CATEGORIES.each { category ->
            def iconNameSet = [] as Set
            Utils.file(rootDir, category, "drawable-mdpi").eachFile { f ->
                def iconName = f.name.find(ICON_PATTERN)
                if (iconName != null) {
                    iconNameSet.add(iconName)
                }
            }
            iconNameSet.each { iconName ->
                index[iconName] = category
            }
        }
        index
    }

    final File rootDir
    Map<String, String> index;

    MaterialDesignIcons(File rootDir) {
        this.rootDir = rootDir
    }

    def gitClone() {
        git.exec("clone $URL $rootDir")
    }

    def gitPull() {
        git.exec("-C $rootDir pull origin master")
    }

    def getIndexFile() {
        new File(rootDir, INDEX_FILE_NAME)
    }

    def saveIndex() {
        getIndexFile().text = new JsonBuilder(createIndex()).toString()
    }

    Map<String, String> getIndex() {
        if (index == null) {
            if (!getIndexFile().isFile()) {
                saveIndex()
            }
            index = new JsonSlurper().parseText(getIndexFile().text) as Map<String, String>
        }
        index
    }

    Map<String, List<Icon>> groupIconsByDensity(Asset asset) {
        def groupIcons = [:]

        listIcons(asset).each { icon ->
            if (!groupIcons.containsKey(icon.density)) {
                groupIcons[icon.density] = []
            }
            groupIcons[icon.density] << icon
        }

        groupIcons
    }

    List<Icon> listIcons(Asset asset) {
        def iconSpec = asset.iconSpec

        def icons = []
        iconSpec.names.each { name ->
            def category = index[name]
            iconSpec.densities.each { density ->
                iconSpec.colors.each { color ->
                    iconSpec.sizes.each { size ->
                        icons << new Icon(
                            name: name,
                            color: color,
                            size: size,
                            density: density,
                            category: category
                        )
                    }
                }
            }
        }

        icons
    }

    def installTo(Asset asset, AndroidProject androidProject) {
        groupIconsByDensity(asset).each { density, icons ->
            def iconDir = androidProject.iconDirOf(density)
            if (!iconDir.isDirectory()) {
                iconDir.mkdirs()
            }

            androidProject.copy {
                from icons.collect { it.toFile() }
                into iconDir
            }
        }
    }

    def uninstallFrom(Asset asset, AndroidProject androidProject) {
        groupIconsByDensity(asset).each { density, icons ->
            def iconDir = androidProject.iconDirOf(density)
            if (iconDir.isDirectory()) {
                icons.each { icon ->
                    new File(iconDir, icon.buildFileName()).delete()
                }
            }
        }
    }

    def installIcons(String density, List<File> icons) {
    }

    class Icon {
        String name
        String color
        String size
        String density
        String category

        File toFile() {
            Utils.file(rootDir, category, "drawable-$density", buildFileName())
        }

        String buildFileName() {
            "${name}_${color}_${size}.png"
        }
    }
}
