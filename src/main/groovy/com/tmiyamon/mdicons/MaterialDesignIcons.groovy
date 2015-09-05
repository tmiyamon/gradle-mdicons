package com.tmiyamon.mdicons

import com.tmiyamon.mdicons.cmd.Git

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class MaterialDesignIcons {
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
    final File workDir

    Map<String, String> index;

    MaterialDesignIcons(File rootDir, File workDir = DEFAULT_WORK_DIR) {
        this.rootDir = rootDir
        this.workDir = workDir
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

    def installTo(List<Asset> assets, AndroidProject androidProject) {
        Utils.workAt(workDir) { File workingDir ->
            assets
                .collect { listIcons(it) }
                .flatten()
                .toSet()
                .groupBy { it.density }
                .each { density, icons ->
                    def iconDir = androidProject.iconDirOf(density)
                    if (!iconDir.isDirectory()) {
                        iconDir.mkdirs()
                    }

                    androidProject.copy {
                        from icons.collect { getOrCreateFileAt(it, workingDir) }
                        into iconDir
                    }
                }
        }
    }

    File createFileAt(Icon icon, File workDir) {
        def workFile = icon.toFile(workDir)

        if (!workFile.isFile()) {
            BufferedImage baseIconImage = ImageIO.read(icon.newWithColor("white").toFile(rootDir))
            int w = baseIconImage.width
            int h = baseIconImage.height

            BufferedImage newIconImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

            (0..w-1).each { x ->
                (0..h-1).each { y ->
                    int[] pixel = baseIconImage.getRaster().getPixel(x, y, null as int[])
                    int color = pixel[0]
                    int alpha = pixel[1]

                    newIconImage.getRaster().setPixel(x, y, [0, 0, 255, alpha] as int[])
                }
            }

            workFile.parentFile.mkdirs()
            ImageIO.write(newIconImage, "png", workFile)
        }

        workFile
    }

    File getOrCreateFileAt(Icon icon, File workDir) {
        def file = icon.toFile(rootDir)

        if (file.isFile()) {
            file
        } else {
            createFileAt(icon, workDir)
        }
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
