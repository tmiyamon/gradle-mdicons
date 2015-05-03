package com.tmiyamon.mdicons


class GSpec {
    static def SPECS = []

    static def describe(Class clazz, Closure cls) {
        assert cls != null
        assert clazz != null

        SPECS << new Specification(clazz: clazz, cls: cls)
    }

    static def run() {
        SPECS.each { spec ->
            spec.run()
        }
    }

    static trait Descriptable {
        String description
    }

    static trait Closureable {
        Closure cls

        def run() {
            with(cls)
        }
    }

    static trait Surroundable {
        def befores = [ each: [], all: []]
        def afters = [ each: [], all: []]

        def before(kind, Closure cls) {
            if (kind in befores) {
                befores[kind] << cls
            }
        }

        def after(kind, Closure cls) {
            if (kind in afters) {
                afters[kind] << cls
            }
        }
    }

    static class Describe implements Descriptable, Surroundable, Closureable  {
        def describes = []
        def its = []

        def describe(String description, Closure cls) {
            describes << new Describe(description: description, cls: cls)
        }

        def it_(String description, Closure cls) {
            its << new It(description: description, cls: cls)
        }

        def run() {
            Closureable.super.run()
            describes.each { it.run() }
            its.each { it.run() }
        }
    }

    static class Specification extends Describe {
        def clazz
    }

    static class It implements Descriptable, Surroundable, Closureable {
        def expect(actual) {
            new Expect(actual: actual)
        }

        def eq(expected) {
            new Eq(expected: expected)
        }
    }

    static class Expect {
        def actual

        def to(matcher) {
            assert matcher.match(actual)
        }
    }

    trait Matcher {
        def expected

        abstract def match(actual)
    }


    static class Eq implements Matcher {
        @Override
        def match(actual) {
            expected == actual
        }
    }

    static class Mock {
        def receiver
        def returnValue

        def andReturn(value) {
            returnValue = value
        }
    }

    static void main(String[] args) {
        GSpec.describe(String) {
            describe("true") {
                it_("returns true") {
                    expect(true).to eq(true)
                }
            }
        }
        GSpec.run()
    }

}



