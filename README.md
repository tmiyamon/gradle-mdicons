# Gradle Material Design Icons Plugin

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-gradle--mdicons-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1334)

This plugin will manage Google's official [material design icons](https://github.com/google/material-design-icons) in your build.gradle. The plugin copies specified icons from the material design icon repository into your application allowing you to focus on the png icons only.

This plugin creates a meta file, `.mdicons`, to save settings and avoid evaluation during every build. Changes are automatically detected and icons are updated based on the changes.

Is writing regex painful? A support tool is being written, please be patient.

## Usage

```groovy
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.tmiyamon:gradle-mdicons:0.1.0'
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

Specify the icon(s) name you want to use in your application with a regex pattern. Failure to specify a pattern will result in no icons being added.

The following pattern

```groovy
mdicons {
    pattern '(refresh|search)_white_24dp'
}
```

results in

![result](/gradle-mdicons-result.png)

### Pattern

The regex `pattern` matches the file name of icons, but does not match the icon type such as 'navigation', 'action' etc. The Default is pattern is `null` and will do nothing.

Google's [material design icons repository](https://github.com/google/material-design-icons) follows a naming convention of `${iconname}_${color}_${size}`. Where the `color` is `black`, `white` or `grey600` and the `size` is `18dp`, `24dp`, `36dp` or `48dp`.

The icon(s) matching the regex pattern will be copied for all screen densities present in the [material design icons repository](https://github.com/google/material-design-icons) (hdpi, mdpi, xhdpi, xxhdpi and xxxhdpi).
