# Gradle Material Design Icons Plugin

This plugin will manage [google's official material design icons](https://github.com/google/material-design-icons) in your build.gradle.
The plugin copy specified icons from the cloned repository into your application.

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

The plugin will clone the material design icons repository in your local environment first, and then refer to it for later build cycle.
So, the first build will take a while for cloning.

## Configuration

You should specify icon name you want to use in your application with regex pattern. If you don't do it, the plugin do nothing.

```groovy
mdicons {
    pattern '(refresh|search)_white'
}
```

- The regex `pattern` matches the name of icons, and does not match the icon type such as 'navigation', 'action' etc. Default is `null` and then do nothing.

