package com.tmiyamon.mdicons.ext

import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals

class AnySpecs: Spek() { init {
    class AnySample(var a: String)

    given("Any") {
        on("#tap") {
            it("returns itself with calling block") {
                assertEquals((AnySample("a").tap{ o -> (o as AnySample).a = "test" } as AnySample).a, "test")
            }
        }
    }
}}

