package com.tmiyamon.mdicons

import groovy.transform.AutoClone
import groovy.transform.Canonical
import groovy.transform.ToString
import org.apache.commons.io.filefilter.RegexFileFilter
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by tmiyamon on 4/26/15.
 */
class MaterialDesignIconsRepository {
    static final Logger logger = LoggerFactory.getLogger(MaterialDesignIconsRepository.canonicalName)
    static final String URL = 'git@github.com:google/material-design-icons.git'

    static final def DENSITIES = ['mdpi', 'hdpi', 'xhdpi', 'xxhdpi', 'xxxhdpi']
    static final def CATEGORIES = ['action', 'alert', 'av', 'communication', 'content', 'device', 'editor', 'file', 'hardware', 'image', 'maps', 'navigation', 'notification', 'social', 'toggle']
    static final def BASE_COLOR = 'white'
    static final def BASE_DENSITY = DENSITIES[0]

    static densityDirName(String density) {
        "drawable-${density}"
    }

    File rootDir

    MaterialDesignIconsRepository(File rootDir) {
        this.rootDir = rootDir
    }

    MaterialDesignIconsRepository(String rootDirPath) {
        this(new File(rootDirPath))
    }

    def cloneTo() {
        def sout = new StringBuilder()
        def serr = new StringBuilder()
        def p = "git clone ${URL} ${rootDir.absolutePath}".execute()
        p.consumeProcessOutput(sout, serr)

        def ret = p.waitFor()
        if (ret != 0) {
            logger.warn(serr.toString())
        }
    }

    def listBaseIcons() {
        def ret = []
        eachIconMatch(CATEGORIES, [BASE_DENSITY], ~/.*${BASE_COLOR}.*/) {
            ret << it
        }
        ret
    }

    def eachIconDir(def categories, def densities, Closure closure) {
        categories.each { c ->
            densities.each { d ->
                closure.call(c, d, new File(rootDir, "/${c}/${densityDirName(d)}"))
            }
        }
    }

    def eachIconFile(def categories, def densities, Closure closure) {
        eachIconDir(categories, densities) { category, density, dir ->
            dir.eachFile { file ->
                closure.call(category, density, dir, file)
            }
        }
    }

    def eachIconFileMatch(def categories, def densities, def matcher, Closure closure) {
        eachIconDir(categories, densities) { category, density, dir ->
            dir.eachFileMatch(matcher) { file ->
                closure.call(category, density, dir, file)
            }
        }
    }

    def eachIcon(def categories, def densities, Closure closure) {
        eachIconFile(categories, densities) { category, density, dir, file ->
            closure.call(newIcon(category, density, file.name))
        }
    }

    def eachIconMatch(def categories, def densities, def matcher, Closure closure) {
        eachIconFileMatch(categories, densities, matcher) { category, density, dir, file ->
            closure.call(newIcon(category, density, file.name))
        }
    }

    def copyIconFileMatch(Project project, def pattern) {
        //TODO improve performance to copy once
        eachIconDir(CATEGORIES, DENSITIES) { category, density, srcDir ->
            def dstDir = project.file("src/main/res/${densityDirName(density)}")
            if (!dstDir.exists()) {
                dstDir.mkdirs()
            }
            project.copy {
                from srcDir
                include { it.name =~ pattern }
                into dstDir
            }
        }

//        def targetIcons = []
//        eachIconMatch(CATEGORIES, DENSITIES, pattern) {
//            targetIcons << it
//        }
//        def iconGroupsByDensity = targetIcons.groupBy{ it.density }
//
//        iconGroupsByDensity.each { density, icons ->
//            def dstDir = project.file("src/main/res/${densityDirName(density)}")
//            if (!dstDir.exists()) {
//                dstDir.mkdirs()
//            }
//
//            println icons*.toFile()
//
//            project.copy {
//                from icons*.toFile()
//                into dstDir
//            }
//        }
    }

    def newIcon(String category, String _density, String fileName) {
        def (name, color, size, ext) = Icon.parseFileName(fileName)
        def density = _density.find(/${DENSITIES.join("|")}/)
        new Icon(
            category: category,
            density: density,
            name: name,
            color: color,
            size: size,
            ext: ext
        )
    }

    @AutoClone
    @Canonical
    @ToString(includeNames = true)
    class Icon {
        String category
        String density
        String name
        String color
        String size
        String ext

        def getFileName() {
            "${name}_${color}_${size}_${ext}"
        }

        def toFile() {
            new File(rootDir, "${category}/${density}/${fileName}")
        }

        static def parseFileName(String fileName) {
            def ret = []

            (fileName =~ /(.+)_([^_]+)_([^_]+)\.(.+)/).each { all, name, color, size, ext ->
                ret = [name, color, size, ext]
            }

            ret
        }
    }
}
