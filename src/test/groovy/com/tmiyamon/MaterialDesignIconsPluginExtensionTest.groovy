package com.tmiyamon

import org.junit.Before
import org.junit.Test

/**
 * Created by tmiyamon on 12/26/14.
 */
class MaterialDesignIconsPluginExtensionTest {
    MaterialDesignIconsPluginExtension ext = new MaterialDesignIconsPluginExtension()

    @Test
    public void isEqualTo_returnsTrueIfAllSaveFieldAreTheSame() {
        def ext2 = new MaterialDesignIconsPluginExtension()
        assert ext2.isEqualTo(ext)
    }
    @Test
    public void isEqualTo_returnsFalseIfAnySaveFieldAreDifferent() {
        def ext2 = new MaterialDesignIconsPluginExtension()
        ext2.pattern = "test"
        assert !ext2.isEqualTo(ext)
    }
}
