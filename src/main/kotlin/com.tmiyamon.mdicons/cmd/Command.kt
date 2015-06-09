package com.tmiyamon.mdicons.cmd

import com.tmiyamon.mdicons.ext.debug
import com.tmiyamon.mdicons.ext.pathJoin
import java.io.File
import java.util.NoSuchElementException

fun exec(vararg strs: String): Int {
    return ProcessBuilder(*strs).start().waitFor()
}

interface Command {
    fun searchPath() = arrayOf("/usr/bin", "/usr/local/bin")

    fun findBin(binName: String): String {
        //TODO support windows

        if (exec("which", binName) == 0) {
            debug("Found", binName, "in env path")
            return binName
        }
        try {
            return searchPath()
                    .map { File("$it/${binName}") }
                    .first { it.exists() }
                    .getAbsolutePath()

        } catch (e: NoSuchElementException) {
            throw Exception("Required ${binName} but not found in ${searchPath().toArrayList()}.")
        }
    }

    class Executor(val bin: String, val subCmd: String = "") {
        private val args = arrayListOf<String>()
        private val options = arrayListOf<List<String>>()

        fun arg(vararg newArgs: Any): Executor {
            args.addAll(newArgs.map { it.toString() })
            return this
        }

        fun option(vararg newOptions: Any): Executor {
            options.add(newOptions.map { it.toString() })
            return this
        }

        fun exec(): Int {
            val commandFragments = (arrayListOf(bin, subCmd) + options.flatten() + args).filter { it.isNotEmpty() }
            return exec(*commandFragments.toTypedArray())
        }
    }
}

