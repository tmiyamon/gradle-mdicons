package com.tmiyamon.mdicons.cmd

class ImageMagick: Command {
    fun convert(): Command.Executor {
        return Command.Executor(findBin("convert"))
    }
}


