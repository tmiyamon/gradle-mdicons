package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import java.io.File
import kotlin.test.assertEquals

class FileSpecs : Spek() { init {
    given("File") {
        on(".pathJoin") {
            it("returns path with path fragments") {
                val paths = array("a", "b", "c")
                assertEquals(pathJoin(*paths), "a${File.separator}b${File.separator}c")
            }
        }
    }
}}