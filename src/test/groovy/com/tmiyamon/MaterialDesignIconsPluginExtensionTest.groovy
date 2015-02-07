package com.tmiyamon

import org.junit.Before
import org.junit.Test

/**
 * Created by tmiyamon on 1/17/15.
 */
class MaterialDesignIconsPluginExtensionTest {
    def ext

    @Before
    public void before() {
        ext = new MaterialDesignIconsPluginExtension()
    }


    @Test
    public void equals_worksWell() {
        def ext1 = new MaterialDesignIconsPluginExtension()
        def ext2 = new MaterialDesignIconsPluginExtension()
        ext1.cachePath = 'a'
        ext1.resourcePath = 'b'
        ext1.pattern 'c'
        ext1.pattern 'd'
        ext2.cachePath = 'a'
        ext2.resourcePath = 'b'
        ext2.pattern 'c'
        ext2.pattern 'd'

        assert ext1 == ext2
    }

    @Test
    public void equals_returnsTrueIfOrderOfPatternsIsDifferent() {
        def ext1 = new MaterialDesignIconsPluginExtension()
        def ext2 = new MaterialDesignIconsPluginExtension()
        ext1.cachePath = 'a'
        ext1.resourcePath = 'b'
        ext1.pattern 'c'
        ext1.pattern 'd'
        ext2.cachePath = 'a'
        ext2.resourcePath = 'b'
        ext2.pattern 'd'
        ext2.pattern 'c'

        assert ext1 == ext2
    }

    @Test
    public void equals_returnFalseIfPatternsAreDifferentSize() {
        def ext1 = new MaterialDesignIconsPluginExtension()
        def ext2 = new MaterialDesignIconsPluginExtension()
        ext1.cachePath = 'a'
        ext1.resourcePath = 'b'
        ext1.pattern 'c'
        ext1.pattern 'd'
        ext2.cachePath = 'a'
        ext2.resourcePath = 'b'
        ext2.pattern 'd'
        ext2.pattern 'c'
        ext2.pattern 'e'

        assert ext1 != ext2
    }

    @Test
    public void equals_returnFalseIfPatternsHaveDifferentElements() {
        def ext1 = new MaterialDesignIconsPluginExtension()
        def ext2 = new MaterialDesignIconsPluginExtension()
        ext1.cachePath = 'a'
        ext1.resourcePath = 'b'
        ext1.pattern 'c'
        ext1.pattern 'd'
        ext2.pattern 'f'
        ext2.cachePath = 'a'
        ext2.resourcePath = 'b'
        ext2.pattern 'd'
        ext2.pattern 'c'
        ext2.pattern 'e'

        assert ext1 != ext2
    }

    @Test
    public void toMap_returnsFieldsMap() {
        ext.cachePath = 'a'
        ext.resourcePath = 'b'
        ext.pattern 'c'
        ext.pattern 'd'

        def map = ext.toMap()
        assert map['cachePath'] == 'a'
        assert map['resourcePath'] == 'b'
        assert map['patterns'] == ['c', 'd']
    }

    @Test
    public void toJson_returnsFieldsJson() {
        ext.cachePath = 'a'
        ext.resourcePath = 'b'
        ext.pattern 'c'
        ext.pattern 'd'

        assert '{"cachePath":"a","resourcePath":"b","patterns":["c","d"]}', ext.toJson()
    }


    @Test
    public void buildPattern_returnsJoindPatternsWithPipe() {
        def ext = new MaterialDesignIconsPluginExtension()
        ext.pattern 'a'
        ext.pattern 'b'
        ext.pattern 'c'

        assert '(a|b|c)' == ext.buildPattern()
    }

    @Test
    public void buildPattern_returnsNullIfPatternIsEmptryArray() {
        assert null == ext.buildPattern()
    }

    @Test
    public void buildPattern_returnsNullIfPatternIsNull() {
        ext.patterns = null
        assert null == ext.buildPattern()
    }

    @Test
    public void pattern_addsToPatternIfPatternIsNotEmpty() {
        ext.pattern 'a'
        assert ext.patterns == ['a']
    }

    @Test
    public void pattern_doesNotAddToPatternIfPatternIsEmpty() {
        ext.pattern ''
        assert ext.patterns == []
    }

    @Test
    public void pattern_doesNotAddToPatternIfPatternIsNull() {
        ext.pattern null
        assert ext.patterns == []
    }
}
