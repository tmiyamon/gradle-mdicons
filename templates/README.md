# Gradle Material Design Icons Plugin

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-gradle--mdicons-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1334)
[![Build Status](https://travis-ci.org/tmiyamon/gradle-mdicons.svg?branch=master)](https://travis-ci.org/tmiyamon/gradle-mdicons)

This plugin will manage Google's official [material design icons](https://github.com/google/material-design-icons) in your build.gradle. The plugin copies specified icons from the material design icon repository into your application allowing you to focus on the png icons only.

The working sample project is [here](https://github.com/tmiyamon/gradle-mdicons-sample).

## Usage

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.tmiyamon:gradle-mdicons:<%= version %>'
  }
}

repositories {
  jcenter()
}

apply plugin: 'com.android.application'
apply plugin: 'com.tmiyamon.mdicons'
```

The plugin will clone the material design icons repository to your local environment first, and then refer to it for later builds. The first build will take a while as a result of the clone.

## Configuration

```groovy
mdicons {
    defcolor 'mycolor', '#9804d9'

    assets {
        nav {
            densities "mdpi", "xxxhdpi"
            colors "mycolor"
            sizes  "18dp", "36dp"
            names  "camera", "search"
        }

        toolbar {
            colors "primary", "secondary" // defined in colors.xml
            sizes  "18dp"
            names  "home"
        }
    }
}
```

Google's [material design icons repository](https://github.com/google/material-design-icons) follows a naming convention of `*/drawable-{{density}}/ic_{{name}}_{{color}}_{{size}}dp.png`.

The blocks in `assets` can have any name and you can define `colors`(required), `sizes`(required), `names`(required) and `densities`(optional) in it, and then the plugin install icons by the combination of them into your project. No specific `densities` means `mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi`.

## Icon tinting

The icon tinting is supported. The plugin gets the color information from the values of `defcolor` and the `src/main/res/values/colors.xml` file. You can use these color names for `names` in asset block.

At installation of icons, when trying to get a specific color icon fails, the plugin try to convert white one to the color one and install it.

## Tasks

### installAssets

Install assets with supporting tinting. The destination directory is `src/main/res-mdicons` which will be automatically added to the resource path set. When successfully installed, the icons will be reffered by `R.mipmap`. If no problem but you cannot refer them, running `gradle processResources` may help you.

### uninstallAssts

Uninstall assets by deleting destination directory.

### listColors

You can check out the available colors for `colors` in assets block.

### syncRepository

Mainly implicitly used but you can explicitly clone or pull the material design repostiory on your system by running this task.


