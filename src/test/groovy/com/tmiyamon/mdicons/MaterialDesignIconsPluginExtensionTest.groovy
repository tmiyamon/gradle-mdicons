//package com.tmiyamon.mdicons
//
//import com.tmiyamon.mdicons.mdicons.MaterialDesignIconsPluginExtension
//import org.junit.Before
//import org.junit.Test
//
///**
// * Created by tmiyamon on 1/17/15.
// */
//class MaterialDesignIconsPluginExtensionTest {
//    def ext
//
//    @Before
//    public void before() {
//        ext = new MaterialDesignIconsPluginExtension()
//    }
//
//    @Test
//    public void toMap_returnsFieldsMap() {
//        ext.cachePath = 'a'
//        ext.resourcePath = 'b'
//        ext.pattern 'c'
//        ext.pattern 'd'
//        ext.group name: 'e', color: 'f', size: 'g'
//        ext.group name: 'h', color: 'i', size: 'j'
//
//        def map = ext.toMap()
//        assert map['cachePath'] == 'a'
//        assert map['resourcePath'] == 'b'
//        assert map['patterns'] == ['c', 'd']
//        assert map['groups'] == [[name: 'e', color: 'f', size: 'g'], [name: 'h', color:'i', size:'j']]
//    }
//
//    @Test
//    public void toJson_returnsFieldsJson() {
//        ext.cachePath = 'a'
//        ext.resourcePath = 'b'
//        ext.pattern 'c'
//        ext.pattern 'd'
//        ext.group name: 'e', color: 'f', size: 'g'
//        ext.group name: 'h', color: 'i', size: 'j'
//
//        assert '{"cachePath":"a","resourcePath":"b","patterns":["c","d"],"groups":[{"name":"e","color":"f","size":"g"},{"name":"h","color":"i","size":"j"}]', ext.toJson()
//    }
//
//    @Test
//    public void group_addsGroupWithNonEmptyArguments() {
//        ext.group name:'a', color:'b', size:'c'
//
//        assert ext.groups == [[name:'a', color:'b', size:'c']]
//    }
//
//    @Test
//    public void group_ignoresWithMissingName() {
//        ext.group color:'b', size:'c'
//
//        assert ext.groups == []
//    }
//
//    @Test
//    public void group_ignoresWithMissingColor() {
//        ext.group name:'a', size:'c'
//
//        assert ext.groups == []
//    }
//
//    @Test
//    public void group_ignoresWithMissingSize() {
//        ext.group name:'a', color:'b'
//
//        assert ext.groups == []
//    }
//
//    @Test
//    public void buildPattern_returnsJoindPatternsWithPipe() {
//        def ext = new MaterialDesignIconsPluginExtension()
//        ext.pattern 'a'
//        ext.pattern 'b'
//        ext.pattern 'c'
//
//        assert '(a|b|c)' == ext.buildPattern()
//    }
//
//    @Test
//    public void buildPattern_returnsNullIfPatternIsEmptryArray() {
//        assert null == ext.buildPattern()
//    }
//
//    @Test
//    public void buildPattern_returnsNullIfPatternIsNull() {
//        ext.patterns = null
//        assert null == ext.buildPattern()
//    }
//
//    @Test
//    public void pattern_addsToPatternIfPatternIsNotEmpty() {
//        ext.pattern 'a'
//        assert ext.patterns == ['a']
//    }
//
//    @Test
//    public void pattern_doesNotAddToPatternIfPatternIsEmpty() {
//        ext.pattern ''
//        assert ext.patterns == []
//    }
//
//    @Test
//    public void pattern_doesNotAddToPatternIfPatternIsNull() {
//        ext.pattern null
//        assert ext.patterns == []
//    }
//}
