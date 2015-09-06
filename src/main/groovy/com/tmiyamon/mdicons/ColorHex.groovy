package com.tmiyamon.mdicons

class ColorHex {
    String hex

    ColorHex(String hex) {
        this.hex = hex
    }

    List<Integer> rgb() {
        def i = Integer.decode(hex).intValue()
        [(i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF]
    }

    List<Integer> rgba(int a) {
        rgb() + [a]
    }

}
