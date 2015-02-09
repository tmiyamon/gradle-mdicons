package com.tmiyamon

import org.junit.Test

/**
 * Created by tmiyamon on 2/10/15.
 */
class IconTest {
    @Test
    void constructor_buildsIcon() {
        def icon = new Icon('a_a_a','b','c', 'd')
        assert icon.name == 'a_a_a'
        assert icon.color == 'b'
        assert icon.size == 'c'
        assert icon.ext == 'd'
    }

    @Test
    void from_buildsIcon() {
        def icon = Icon.from('a_a_a','b','c', 'd')
        assert icon.name == 'a_a_a'
        assert icon.color == 'b'
        assert icon.size == 'c'
        assert icon.ext == 'd'
    }

    @Test
    void from_buildsIconWithFile() {
        def icon = Icon.from(new File('/tmp/a_a_a_b_c.d'))
        assert icon.name == 'a_a_a'
        assert icon.color == 'b'
        assert icon.size == 'c'
        assert icon.ext == 'd'
    }

    @Test
    void fileName_returnsFileName() {
        assert 'a_a_a_b_c.d' == Icon.from(new File('a_a_a_b_c.d')).fileName
    }

    @Test
    void variantFiles_returnsVariantFiles() {
        def name = 'a_a_a_b_c.d'
        def expect = Icon.DENSITIES.collect { "/drawable-${it}/${name}"}
        def actual = Icon.from(new File(name)).getVariantFiles(new File('/')).collect {it.absolutePath}
        assert expect == actual
    }

    @Test
    void canonical_returnsCanonical() {
        def icon = Icon.from(new File('a_a_a_b_c.d')).canonical
        assert 'a_a_a' == icon.name
        assert Icon.CANONICAL_COLOR == icon.color
        assert 'c' == icon.size
        assert 'd' == icon.ext
    }
}
