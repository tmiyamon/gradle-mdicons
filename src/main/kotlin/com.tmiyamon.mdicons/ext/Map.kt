package com.tmiyamon.mdicons.ext

fun <K,T> Map<K,T>.slice(vararg keys: K): Map<K,T> {
    return keys.filter { key -> this.containsKey(key) }.toMapWith { key -> this[key] as T }
}

fun <K,T> Map<K,T>.valuesAt(vararg keys: K): List<T> {
    return keys.filter { containsKey(it) && get(it) != null }.map { get(it) as T }
}
