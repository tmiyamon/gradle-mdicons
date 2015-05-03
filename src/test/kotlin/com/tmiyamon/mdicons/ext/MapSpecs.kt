package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class MapSpecs: Spek() { init {
    given("Map") {
        on("#slice") {
            it("returns new map with given keys and their values") {
                val map = mapOf("a" to 1, "b" to 2, "c" to 3)
                assertEquals(map.slice("a","c"), mapOf("a" to 1, "c" to 3))
            }
        }
    }
}}
