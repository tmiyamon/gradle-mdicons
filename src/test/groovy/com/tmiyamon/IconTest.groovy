package com.tmiyamon

import com.tmiyamon.mdicons.Icon
import org.junit.Before
import org.junit.Test

/**
 * Created by tmiyamon on 2/10/15.
 */
class IconTest {
    Icon icon
    File cacheDir
    File resourceDir

    @Before
    void beforeEach() {
        icon = new Icon('category', 'density', 'name', 'color', 'size', 'ext')
        cacheDir = new File('/cacheDir')
        resourceDir = new File('/resourceDir')
    }

    @Test
    void fileName_returnsBuiltFileName() {
        assert "name_color_size.ext" == icon.fileName
    }

    @Test
    void variants_returnsVariants() {
        def expect = [
            icon.newWithDensity('drawable-mdpi'),
            icon.newWithDensity('drawable-hdpi'),
            icon.newWithDensity('drawable-xhdpi'),
            icon.newWithDensity('drawable-xxhdpi'),
            icon.newWithDensity('drawable-xxxhdpi')
        ]
        assert expect == icon.variants
    }

    @Test
    void getCacheFile_returnsTheFileInCacheDirForTheIcon() {
        assert new File(cacheDir, "category/density/name_color_size.ext") == icon.getCacheFile(cacheDir)
    }

    @Test
    void getCacheVariantFiles_returnsTheVariantFilesInCacheDirForTheIcon() {
        def expect = [
                icon.newWithDensity('drawable-mdpi').getCacheFile(cacheDir),
                icon.newWithDensity('drawable-hdpi').getCacheFile(cacheDir),
                icon.newWithDensity('drawable-xhdpi').getCacheFile(cacheDir),
                icon.newWithDensity('drawable-xxhdpi').getCacheFile(cacheDir),
                icon.newWithDensity('drawable-xxxhdpi').getCacheFile(cacheDir)
        ]
        assert expect == icon.getCacheVariantFiles(cacheDir)
    }

    @Test
    void getProjectResourceFile_returnsTheFileInProjectResourceDirForTheIcon() {
        assert new File(resourceDir, "density/name_color_size.ext") == icon.getProjectResourceFile(resourceDir)
    }

    @Test
    void getProjectResourceVariantFiles_returnsTheVariantFilesInProjectResourceDirForTheIcon() {
        def expect = [
                icon.newWithDensity('drawable-mdpi').getProjectResourceFile(resourceDir),
                icon.newWithDensity('drawable-hdpi').getProjectResourceFile(resourceDir),
                icon.newWithDensity('drawable-xhdpi').getProjectResourceFile(resourceDir),
                icon.newWithDensity('drawable-xxhdpi').getProjectResourceFile(resourceDir),
                icon.newWithDensity('drawable-xxxhdpi').getProjectResourceFile(resourceDir)
        ]
        assert expect == icon.getProjectResourceVariantFiles(resourceDir)
    }

    @Test
    void newCanonical_returnsCanonicalIconForTheIcon() {
        assert icon.newWithColor(Icon.CANONICAL_COLOR).newWithDensity(Icon.CANONICAL_DENSITY) == icon.newCanonical()
    }

    @Test
    void newWithColor_returnsNewIconWithColor() {
        def actual = icon.newWithColor('test')
        icon.color = 'test'
        assert icon == actual
    }

    @Test
    void newWithDensity_returnsNewIconWithDensity() {
        def actual = icon.newWithDensity('test')
        icon.density = 'test'
        assert icon == actual
    }

    @Test
    void isCanonicalColor_returnsTrueIfColorOfIconIsCanonical() {
        icon.color = Icon.CANONICAL_COLOR
        assert icon.isCanonicalColor()
    }

    @Test
    void isCanonicalColor_returnsFalseIfColorOfIconIsNotCanonical() {
        assert !icon.isCanonicalColor()
    }

    @Test
    void isCanonicalDensity_returnsTrueIfDensityOfIconIsCanonical() {
        icon.density = Icon.CANONICAL_DENSITY
        assert icon.isCanonicalDensity()
    }

    @Test
    void isCanonicalDensity_returnsFalseIfDensityOfIconIsNotCanonical() {
        assert !icon.isCanonicalDensity()
    }
}
