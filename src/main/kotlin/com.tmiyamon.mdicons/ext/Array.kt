package com.tmiyamon.mdicons.ext

import java.util.LinkedHashMap

public fun <T, K> Array<out K>.toMapWith(block: () -> T): Map<K, T> {
    val result = LinkedHashMap<K, T>()
    for (element in this) {
        result.put(element, block())
    }
    return result
}
