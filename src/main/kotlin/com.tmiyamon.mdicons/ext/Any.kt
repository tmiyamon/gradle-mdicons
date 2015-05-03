package com.tmiyamon.mdicons.ext

fun Any.tap(block: (Any) -> Unit): Any {
    block(this)
    return this
}

