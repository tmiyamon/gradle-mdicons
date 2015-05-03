package com.tmiyamon.mdicons.mdicons

/**
 * Created by tmiyamon on 2/11/15.
 */
class ImageMagick {
    static def SEARCH_PATH = [
        '/usr/bin',
        '/usr/local/bin'
    ]

    static def findBin() {
        SEARCH_PATH.collect { "${it}/convert" }.find { new File(it).exists() }
    }

    def bin
    def args
    def options = []

    ImageMagick() {
        this.bin = findBin()
    }

    def args(src, dst) {
        this.args = [src, dst]
        this
    }

    def option(...option) {
        this.options << option
        this
    }

    def isAvailable() {
        this.bin && this.args
    }

    def exec() {
        if (isAvailable()) {
            def cmd = [
                this.bin,
                this.options,
                this.args
            ].flatten().join(' ')
            cmd.execute().waitFor()
        }
    }

}
