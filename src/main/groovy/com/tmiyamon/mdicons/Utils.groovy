package com.tmiyamon.mdicons

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
    static List flatList(Object obj) {
        if (obj instanceof List) {
            return obj
        }
        return [obj]
    }

    static boolean isEmpty(String str) {
        return str == null || str.isEmpty()
    }
    static boolean isEmpty(Collection col) {
        return col == null || col.isEmpty()
    }
    static boolean isNotEmpty(String str) {
        return !isEmpty(str)
    }
    static boolean isNotEmpty(Collection col) {
        return !isEmpty(col)
    }


    static def eachIconFiles(File root, Set iconTypes, String pattern, Closure closure) {
        root.eachDirMatch({ iconTypes.contains(it.name) }) {
            it.eachDirMatch({ it.name.startsWith("drawable")}) {
                if (isNotEmpty(pattern)) {
                    it.eachFileMatch({ it.name =~ pattern }, closure)
                } else {
                    it.eachFile(closure)
                }
            }
        }
    }
}
