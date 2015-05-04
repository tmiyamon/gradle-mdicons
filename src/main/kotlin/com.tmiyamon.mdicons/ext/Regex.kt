package com.tmiyamon.mdicons.ext

import java.util.regex.Pattern

fun Pattern.matchGroups(str: String): List<String> {
    val m = matcher(str)

    return if(m.matches()) {
        (1..m.groupCount()).map { i -> m.group(i) }
    } else {
        emptyList()
    }
}

