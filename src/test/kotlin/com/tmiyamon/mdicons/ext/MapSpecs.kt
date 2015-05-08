package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MapSpecs: Spek() { init {
    given("Map") {
        on("#slice") {
            it("returns new map with given keys and their values") {
                val map = mapOf("a" to 1, "b" to 2, "c" to 3)
                assertEquals(map.slice("a","c"), mapOf("a" to 1, "c" to 3))
            }
        }
        on("#valuesAt") {
            it("return only non null values in specified order") {
                val map = mapOf("a" to 1, "b" to null, "c" to 3)
                assertEquals(map.valuesAt("a", "b", "c", "d"), listOf(1, 3))
            }
        }
    }
}}
