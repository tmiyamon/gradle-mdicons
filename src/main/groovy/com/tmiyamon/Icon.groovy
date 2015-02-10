package com.tmiyamon
/**
 * Created by tmiyamon on 2/11/15.
 */
class Icon {
    static def DENSITIES = [
            'drawable-mdpi',
            'drawable-hdpi',
            'drawable-xhdpi',
            'drawable-xxhdpi',
            'drawable-xxxhdpi' ]
    static def CATEGORIES = [
            'action','alert','av','communication','content','device',
            'editor','file','hardware','image','maps','navigation',
            'notification','social','toggle'
    ]
    static def CANONICAL_COLOR = 'white'
    static def CANONICAL_DENSITY = 'drawable-mdpi'

    static Icon fromCache(File cacheFile) {
        def m = (cacheFile.name =~ /(.+)_([^_]+)_([^_]+)\.(.+)/)
        def path = cacheFile.parent.split('/')
        m.matches() ? new Icon(path[-2], path[-1], *m[0][1..-1]) : null;
    }

    static Icon fromProjectResource(File resourceFile) {
        def m = (resourceFile.name =~ /(.+)_([^_]+)_([^_]+)\.(.+)/)
        def path = resourceFile.parent.split('/')
        m.matches() ? new Icon(null, path[-1], *m[0][1..-1]) : null;
    }

    static def eachCacheCanonicalIcons(File cacheDir, Closure closure)  {
        CATEGORIES.each {
            new File(
                cacheDir, "${it}/${CANONICAL_DENSITY}"
            ).eachFileMatch(~".*${CANONICAL_COLOR}.*") {
                Icon icon = Icon.fromCache(it)
                if (icon != null && icon.isCanonicalColor() && icon.isCanonicalDensity()) {
                    closure(icon)
                }
            }
        }
    }

    static def eachCacheCanonicalIconsMatchedToGroups(File cacheDir, List<IconGroup> iconGroups, Closure closure)  {
        def pattern = iconGroups.collect { it.canonicalPattern }.join("|")
        println(pattern)
        eachCacheCanonicalIcons(cacheDir) { Icon icon ->
            if (icon.fileName =~ pattern) {
                closure(icon)
            }
        }
    }


    static def eachProjectResourceIcons(File projectResourceDir, Closure closure) {
        DENSITIES.each {
            new File(projectResourceDir, it).eachFile {
                Icon icon = Icon.fromProjectResource(it)
                if (icon != null) {
                    closure(icon)
                }
            }
        }
    }

    String category
    String density
    String name
    String color
    String size
    String ext

    Icon(String category, String density, String name, String color, String size, String ext) {
        this.category = category
        this.density = density
        this.name = name
        this.color = color
        this.size = size
        this.ext = ext
    }

    def getFileName() {
        "${name}_${color}_${size}.${ext}"
    }

    def getVariants() {
        DENSITIES.collect { newWithDensity(it) }
    }

    def getCacheFile(File cacheRoot) {
        new File(cacheRoot, "${category}/${density}/${fileName}")
    }

    def getCacheVariantFiles(File cacheRoot) {
        DENSITIES.collect { newWithDensity(it).getCacheFile(cacheRoot) }
    }

    def isCacheExists(File cacheRoot) {
        getCacheFile(cacheRoot).exists()
    }

    def getProjectResourceFile(File resourceRoot) {
        new File(resourceRoot, "${density}/${fileName}")
    }

    def getProjectResourceVariantFiles(File resourceRoot) {
        DENSITIES.collect { newWithDensity(it).getProjectResourceFile(resourceRoot) }
    }

    def newCanonical() {
        new Icon(category, CANONICAL_DENSITY, name, CANONICAL_COLOR, size, ext)
    }

    def newWithColor(String newColor) {
        new Icon(category, density, name, newColor, size, ext)
    }

    def newWithDensity(String newDensity) {
        new Icon(category, newDensity, name, color, size, ext)
    }

    def isCanonicalColor() {
        this.color == CANONICAL_COLOR
    }

    def isCanonicalDensity() {
        this.density == CANONICAL_DENSITY
    }

    def generateCache(File cacheDir) {
        def canonicalColorIcon = newWithColor(CANONICAL_COLOR)
        def cmd = "/usr/local/bin/convert ${canonicalColorIcon.getCacheFile(cacheDir)} -fuzz 75% -fill ${color} -opaque white -type TruecolorMatte PNG32:${getCacheFile(cacheDir)}"
        cmd.execute().waitFor()
    }


    @Override
    public String toString() {
        return "IconFile{" +
                "category='" + category + '\'' +
                ", density='" + density + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", ext='" + ext + '\'' +
                '}';
    }
}
