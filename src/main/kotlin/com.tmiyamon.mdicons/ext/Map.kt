package com.tmiyamon.mdicons.ext

fun <K,T> Map<K,T>.slice(vararg keys: K): Map<K,T> {
    return keys.filter { key -> this.containsKey(key) }.toMapWith { key -> this[key] as T }
}
