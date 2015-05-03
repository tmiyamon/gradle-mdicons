package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import java.util.regex.Pattern
import kotlin.test.assertEquals

class RegexSpecs : Spek() { init {
    given("Regex Utility") {
        on(".matchGroup") {
            val value = "a_b_c"

            it("returns match groups list with match group pattern") {
                val pattern = Pattern.compile("""([a-z])_([a-z])_([a-z])""")
                assertEquals(pattern.matchGroups(value), listOf("a", "b", "c"))
            }

            it("returns empty list with not mached group pattern") {
                val pattern = Pattern.compile("""([A-Z])_([A-Z])_([A-Z])""")
                assertEquals(pattern.matchGroups(value), emptyList<String>())
            }
        }
    }
}}