package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class ArraySpecs: Spek() { init {
    given("Array") {
        on("#toMapWith") {
            it("returns new map with calling block") {
                val array = array("a","b")
                assertEquals(array.toMapWith { key -> key + key }, mapOf("a" to "aa", "b" to "bb"))
            }
        }
    }
}}
