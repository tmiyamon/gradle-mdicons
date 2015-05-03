package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class IterableSpecs: Spek() { init {
    given("Iterable") {
        on("#toMapWith") {
            it("returns new map with calling block") {
                val list = listOf("a","b")
                assertEquals(list.toMapWith { key -> key + key }, mapOf("a" to "aa", "b" to "bb"))
            }
        }
    }
}}
