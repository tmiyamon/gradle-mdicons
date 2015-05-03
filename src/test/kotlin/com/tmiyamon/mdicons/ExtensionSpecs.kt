package com.tmiyamon.mdicons

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExtensionSpecs: Spek() { init {
    given("Extension") {
        on("toMap") {
            it("returns representing map with specific keys") {
                val ext = Extension()
                ext.patterns.add("pattern")

                assertEquals(listOf("pattern"), ext.toMap(Extension.KEY_PATTERNS)[Extension.KEY_PATTERNS])
            }
            it("returns representing map which does not contains supported key") {
                val ext = Extension()

                assertFalse(ext.toMap("test").containsKey("test"))
            }
        }
    }
}}

