package com.tmiyamon.mdicons.cmd

class Git: Command {
    companion object {
        val BIN_NAME = "git"
    }

    fun clone(): Command.Executor {
        return Command.Executor(findBin(BIN_NAME), "clone")
    }
}

