package com.tmiyamon.mdicons

/**
 * Created by tmiyamon on 8/29/15.
 */
class Utils {

    static def replacer = { all, _1 -> _1.toUpperCase()[-1] }

    static String camelize(String str) {
        str.toLowerCase().replaceAll(/(_.)/, replacer)
    }

    static String pascalize(String str) {
        str.toLowerCase().replaceAll(/(^.|_.)/, replacer)
    }
}
