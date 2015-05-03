package com.tmiyamon.mdicons.mdicons
//package com.tmiyamon.mdicons
//
//import groovy.transform.AutoClone
//import groovy.transform.Canonical
//import groovy.transform.ToString
//import org.gradle.api.Project
//
///**
// * Created by tmiyamon on 2/11/15.
// */
//@ToString(includeNames = true)
//abstract class Icon {
//    static PluginExt
//
//    static def DENSITIES = [
//            'mdpi', 'hdpi', 'xhdpi', 'xxhdpi', 'xxxhdpi'
//    ].collect {"drawable-${it}"}
//
//    static def CATEGORIES = [
//            'action','alert','av','communication','content','device',
//            'editor','file','hardware','image','maps','navigation',
//            'notification','social','toggle'
//    ]
//    static def CANONICAL_COLOR = 'white'
//    static def CANONICAL_DENSITY = 'drawable-mdpi'
//
////    static Icon fromCache(File cacheFile) {
////        def m = (cacheFile.name =~ /(.+)_([^_]+)_([^_]+)\.(.+)/)
////        def path = cacheFile.parent.split('/')
////        m.matches() ? new Icon(path[-2], path[-1], *m[0][1..-1]) : null;
////    }
////
////    static Icon fromProjectResource(File resourceFile) {
////        def m = (resourceFile.name =~ /(.+)_([^_]+)_([^_]+)\.(.+)/)
////        def path = resourceFile.parent.split('/')
////        m.matches() ? new Icon(null, path[-1], *m[0][1..-1]) : null;
////    }
//
//    static def eachCacheCanonicalIcons(File cacheDir, Closure closure)  {
//        CATEGORIES.each {
//            new File(
//                cacheDir, "${it}/${CANONICAL_DENSITY}"
//            ).eachFileMatch(~".*${CANONICAL_COLOR}.*") {
//                Icon icon = Icon.fromCache(it)
//                if (icon != null && icon.isCanonicalColor() && icon.isCanonicalDensity()) {
//                    closure(icon)
//                }
//            }
//        }
//    }
//
//    static def eachCacheCanonicalIconsMatchedToGroups(File cacheDir, List<IconGroup> iconGroups, Closure closure)  {
//        def pattern = iconGroups*.canonicalPattern.join("|")
//        eachCacheCanonicalIcons(cacheDir) { Icon icon ->
//            if (icon.fileName =~ pattern) {
//                closure(icon)
//            }
//        }
//    }
//
//
//    static def eachProjectResourceIcons(File projectResourceDir, Closure closure) {
//        DENSITIES.each {
//            def iconDirInDensity = new File(projectResourceDir, it)
//            if (iconDirInDensity.isDirectory()) {
//                iconDirInDensity.eachFile {
//                    Icon icon = Icon.fromProjectResource(it)
//                    if (icon != null) {
//                        closure(icon)
//                    }
//                }
//            }
//        }
//    }
//
//    static def parseFileName(String fileName) {
//        def ret = []
//
//        (fileName =~ /(.+)_([^_]+)_([^_]+)\.(.+)/).each { all, name, color, size, ext ->
//            ret = [name, color, size, ext]
//        }
//
//        ret
//    }
//
////    String category
//    String density
//    String color
//    String size
//    String fileName
//
////    Icon(String density, String name, String color, String size, String ext) {
////        this.density = density
////        this.name = name
////        this.color = color
////        this.size = size
////        this.ext = ext
////    }
//
//    Icon() {}
//
//    def getFileName() {
//        "${name}_${color}_${size}.${ext}"
//    }
//
//    def getVariants() {
//        DENSITIES.collect { newWithDensity(it) }
//    }
//
//    def getCacheFile(File cacheRoot) {
//        new File(cacheRoot, "${category}/${density}/${fileName}")
//    }
//
//    def getCacheVariantFiles(File cacheRoot) {
//        DENSITIES.collect { newWithDensity(it).getCacheFile(cacheRoot) }
//    }
//
//    def isCacheExists(File cacheRoot) {
//        getCacheFile(cacheRoot).exists()
//    }
//
//    def getProjectResourceFile(File resourceRoot) {
//        new File(resourceRoot, "${density}/${fileName}")
//    }
//
//    def getProjectResourceVariantFiles(File resourceRoot) {
//        DENSITIES.collect { newWith(density: it).getProjectResourceFile(resourceRoot) }
//    }
//
//    abstract def newCanonical()
//
//    def newWith(Map map) {
//        this.clone().tap {
//            def that = this
//            map.each { k, v ->
//                that."${k}" = v
//            }
//        }
//    }
//
//    def isCanonicalColor() {
//        this.color == CANONICAL_COLOR
//    }
//
//    def isCanonicalDensity() {
//        this.density == CANONICAL_DENSITY
//    }
//
//    def generateCache(File cacheDir) {
//        def canonicalColorIcon = newWithColor(CANONICAL_COLOR)
//        def colorHex = MaterialColor.instance.getHexFrom(color)
//
//        def src = canonicalColorIcon.getCacheFile(cacheDir).absolutePath
//        def dst = "PNG32:${getCacheFile(cacheDir).absolutePath}"
//
//        new ImageMagick()
//            .option("-fuzz", "75%")
//            .option('-fill', colorHex)
//            .option('-opaque', CANONICAL_COLOR)
//            .option('-type', 'TrueColorMatte')
//            .args(src, dst)
//            .exec()
//    }
//}
//
//
//@AutoClone
//@Canonical
//@ToString(includeNames = true, includeSuper = true)
//class ProjectIcon extends Icon {
//    Project project
//
//    static def newWithFile(File file) {
//        def (name, color, size, ext) = parseFileName(file.name)
//        def density = file.parent.split('/')[-1]
//
//        new ProjectIcon(
//            name: name, color: color, size: size, ext: ext, density: density, file: file)
//    }
//
//    @Override
//    def newCanonical() {
//        this.clone().tap {
//            color = CANONICAL_COLOR
//            density = CANONICAL_DENSITY
//        }
//    }
//}
//
//@AutoClone
//@Canonical
//@ToString(includeNames = true, includeSuper = true)
//class MaterialDesignIcon extends Icon {
//    MaterialDesignIconsRepository repository
//    String category
//
//    static def newWithFile(MaterialDesignIconsRepository repository, File file) {
//        def (category, density) = file.parent.split('/')[-2..-1]
//        def (name, color, size, ext) = parseFileName(file.name)
//
//        new MaterialDesignIcon(
//            name: name, color: color, size: size, ext: ext, density: density, category: category, repository: repository)
//    }
//
//    @Override
//    def newCanonical() {
//        this.clone().tap {
//            color = CANONICAL_COLOR
//            density = CANONICAL_DENSITY
//        }
//    }
//
//    def toProjectIcon(Project project) {
//        new ProjectIcon(density: density, name: name, color: color, size: size, ext: ext, project: project)
//    }
//
//    def toFile() {
//    }
//}

