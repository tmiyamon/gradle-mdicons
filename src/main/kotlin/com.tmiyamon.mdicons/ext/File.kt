package com.tmiyamon.mdicons.ext

import java.io.*
import java.util.regex.Pattern

fun pathJoin(vararg paths: String): String {
    return paths.joinToString(File.separator)
}

fun <T> File.withWriter(b: (BufferedWriter) -> T): T {
    val writer = BufferedWriter(FileWriter(this))
    try {
        return b(writer)
    } finally {
        writer.close()
    }
}


fun <T> File.withReader(b: (BufferedReader) -> T): T {
    val reader = BufferedReader(FileReader(this))
    try {
        return b(reader)
    } finally {
        reader.close()
    }
}
