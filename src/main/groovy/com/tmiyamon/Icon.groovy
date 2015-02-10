package com.tmiyamon

/**
 * Created by tmiyamon on 2/9/15.
 */
class Icon {
    static def DENSITIES = [ 'mdpi', 'hdpi', 'xhdpi', 'xxhdpi', 'xxxhdpi' ]
    static def CANONICAL_COLOR = 'white'

    static Icon from(File file) {
        def m = (file.name =~ /(.+)_([^_]+)_([^_]+)\.(.+)/)
        m.matches() ? new Icon(*m[0][1..-1]) : null;
    }

    String name
    String color
    String size
    String ext

    private Icon(String name, String color, String size, String ext) {
        this.name = name
        this.color = color
        this.size = size
        this.ext = ext
    }

    def getFileName() {
        "${name}_${color}_${size}.${ext}"
    }

    def getFile(File parent) {
        new File(parent, fileName)
    }

    def getVariantFiles(File variantRoot) {
        DENSITIES.collect { new File(variantRoot, "drawable-${it}/${fileName}")}
    }

    def eachVariantFiles(File variantRoot, Closure closure) {
        DENSITIES.each { closure(it, new File(variantRoot, "drawable-${it}/${fileName}")) }
    }

    def newWithColor(String newColor) {
        new Icon(name, newColor, size, ext)
    }

    def getCanonical() {
        newWithColor(CANONICAL_COLOR)
    }

    def isCanonical() {
        this.color == CANONICAL_COLOR
    }
}
