# Gradle Material Design Icons Plugin

This plugin will manage [google's official material design icons](https://github.com/google/material-design-icons) in your build.gradle.<br/>
The plugin copy specified icons from the cloned repository into your application.<br/>
Now focus on the png icons only.

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

get result below

![result](/gradle-mdicons-result.png)

### pattern
The regex `pattern` matches the file name of icons, and does not match the icon type such as 'navigation', 'action' etc. Default is `null` and then do nothing.

The google's material design icons repository has the naming rule of the icons like `${iconname}_${color}_${size}`.<br/>
The `color` is `black`, `white` or `grey600`.<br/>
The `size` is `18dp`, `24dp`, `36dp` or `48dp`.<br/>

The matched icon with the regex pattern will be copied for the all screen sizes material design icons has (hdpi, mdpi, xhdpi, xxhdpi and xxxhdpi).
