package com.tmiyamon

/**
 * Created by tmiyamon on 2/9/15.
 */
class Icon {
    static def DENSITIES = [ 'mdpi', 'hdpi', 'xhdpi', 'xxhdpi', 'xxxhdpi' ]
    static def CANONICAL_COLOR = 'white'

    static Icon from(String name, String color, String size, String ext) {
        return new Icon(name, color, size, ext)
    }
    static Icon from(File file) {
        def m = (file.name =~ /(.+)_([^_]+)_([^_]+)\.(.+)/)
        m.matches() ? from(*m[0][1..-1]) : null;
    }

    String name
    String color
    String size
    String ext

    Icon(String name, String color, String size, String ext) {
        this.name = name
        this.color = color
        this.size = size
        this.ext = ext
    }

    def getFileName() {
        "${name}_${color}_${size}.${ext}"
    }

    def getFile(File root) {
        new File(root, fileName)
    }

    def getVariantFiles(File root) {
        DENSITIES.collect { new File(root, "drawable-${it}/${fileName}")}
    }

    def newWithColor(String newColor) {
        from(name, newColor, size, ext)
    }

    def getCanonical() {
        from(name, CANONICAL_COLOR, size, ext)
    }
}
