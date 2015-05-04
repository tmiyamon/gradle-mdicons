package com.tmiyamon.mdicons.ext

fun <T> T.tap(block: (T) -> Unit): T {
    block(this)
    return this
}


