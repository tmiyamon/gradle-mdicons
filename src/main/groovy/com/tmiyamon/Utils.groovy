package com.tmiyamon

/**
 * Created by tmiyamon on 12/25/14.
 */
class Utils {
    static String capitalize(String str) {
        return "${str[0].toUpperCase()}${str.substring(1)}"
    }
    static String getGetterName(String fieldName) {
        return "get${capitalize(fieldName)}"
    }
    static String getSetterName(String fieldName) {
        return "set${capitalize(fieldName)}"
    }
    static def getValue(Object obj, String fieldName) {
        return obj."${getGetterName(fieldName)}"()
    }
    static void setValue(Object obj, String fieldName, def value) {
        obj."${getSetterName(fieldName)}"(value)
    }
}
