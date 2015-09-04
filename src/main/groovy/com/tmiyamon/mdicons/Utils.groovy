package com.tmiyamon.mdicons

class Utils {

    static def replacer = { all, _1 -> _1.toUpperCase()[-1] }

    static String camelize(String str) {
        str.toLowerCase().replaceAll(/(_.)/, replacer)
    }

    static String pascalize(String str) {
        str.toLowerCase().replaceAll(/(^.|_.)/, replacer)
    }

    static String pathjoin(String...paths) {
        paths.join(File.separator)
    }

    static File file(File parent, String...paths) {
        new File(parent, pathjoin(*paths))
    }
}
