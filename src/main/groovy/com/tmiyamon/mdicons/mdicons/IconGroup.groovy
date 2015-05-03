package com.tmiyamon.mdicons.mdicons
/**
 * Created by tmiyamon on 2/11/15.
 */
public class IconGroup {
    String name
    String color
    String size

    def getCanonicalPattern() {
        ".*(${name}).*_${Icon.CANONICAL_COLOR}_${size}"
    }
}
