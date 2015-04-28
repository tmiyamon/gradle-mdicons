package com.tmiyamon.mdicons
/**
 * Created by tmiyamon on 2/11/15.
 */
public class IconGroup {
    String name
    String color
    String size

    IconGroup(Map<String, String> group) {
        this(group['name'], group['color'], group['size'])
    }

    IconGroup(String name, String color, String size) {
        this.name = name
        this.color = color
        this.size = size
    }

    def getCanonicalPattern() {
        ".*(${name}).*_${Icon.CANONICAL_COLOR}_${size}"
    }
}
