package com.tmiyamon.mdicons

import com.tmiyamon.mdicons.cmd.Git
import org.gradle.api.Project

class MaterialDesignIconsRepository {
    static final String URL = "https://github.com/google/material-design-icons.git"

    static final String ROOT_DIR_NAME = ".material_design_icons"
    static final String INDEX_FILE_NAME = ".index"
    static File DEFAULT_ROOT_DIR = Utils.home(ROOT_DIR_NAME)
    static File DEFAULT_WORK_DIR = Utils.home(".mdicons")

    static final List<String> CATEGORIES = [
        "action", "alert", "av", "communication", "content", "device",
        "editor", "file", "hardware", "image", "maps", "navigation",
        "notification", "social", "toggle"
    ]
    static final List<String> DENSITIES = [
        "hdpi", "mdpi", "xhdpi", "xxhdpi", "xxxhdpi"
    ]
    static final String ICON_PATTERN = /(?<=ic_).*(?=_(black|white)_\d+dp.png)/
    static final Git git = new Git()

    static build(Project project, File rootDir = DEFAULT_ROOT_DIR) {
        new MaterialDesignIconsRepository(project, rootDir)
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
    final File workDir
    final MaterialDesignColor materialDesignColor

    Map<String, String> index;

    MaterialDesignIconsRepository(Project project, File rootDir, File workDir = DEFAULT_WORK_DIR) {
        this.rootDir = rootDir
        this.workDir = workDir
        this.materialDesignColor = MaterialDesignColor.populate(project)
    }

    def gitClone() {
        git.exec("clone $URL $rootDir")
    }

    def gitPull() {
        git.exec("-C $rootDir pull origin master")
    }

    Map<String, String> getIndex() {
        if (index == null) {
            index = createIndex()
        }
        index
    }

    List<Icon> listIcons(Asset asset) {
        def iconSpec = asset.iconSpec

        def icons = []
        iconSpec.names.each { name ->
            def category = index[name]
            iconSpec.densities.each { density ->
                iconSpec.colors.each { color ->
                    iconSpec.sizes.each { size ->
                        icons << new Icon(name, color, size, density, category)
                    }
                }
            }
        }

        icons
    }


    class Icon {
        final String name
        final String color
        final String size
        final String density
        final String category

        Icon(String name, String color, String size, String density, String category) {
            this.name = name
            this.color = color
            this.size = size
            this.density = density
            this.category = category
        }

        File toFile(File parent) {
            Utils.file(parent, category, "drawable-$density", buildFileName())
        }

        Icon newWithColor(String newColor) {
            new Icon(name, newColor, size, density, category)
        }

        String buildFileName() {
            "ic_${name}_${color}_${size}.png"
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            Icon icon = (Icon) o

            if (category != icon.category) return false
            if (color != icon.color) return false
            if (density != icon.density) return false
            if (name != icon.name) return false
            if (size != icon.size) return false

            return true
        }

        int hashCode() {
            int result
            result = (name != null ? name.hashCode() : 0)
            result = 31 * result + (color != null ? color.hashCode() : 0)
            result = 31 * result + (size != null ? size.hashCode() : 0)
            result = 31 * result + (density != null ? density.hashCode() : 0)
            result = 31 * result + (category != null ? category.hashCode() : 0)
            return result
        }

    }
}
