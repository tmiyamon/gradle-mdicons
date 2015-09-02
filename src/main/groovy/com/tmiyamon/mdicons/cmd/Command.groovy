package com.tmiyamon.mdicons.cmd

abstract class Command {
    static List<String> SEARCH_PATH = ["/usr/bin", "/usr/local/bin"]
    String binPath

    static String findPath(String binName) {
        if ("which ${binName}".execute().waitFor() == 0) {
            return binName
        }

        try {
            SEARCH_PATH.collect { new File(it, binName) }.find { it.exists() }.getAbsoluteFile()
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Try to find $binName but not found in $SEARCH_PATH")
        }
    }

    int exec(String args) {
        if (binPath == null) {
            binPath = findPath(getBinName())
        }
        def cmd = "$binPath $args"
        println cmd
        cmd.execute().waitFor()
    }

    abstract String getBinName()
}
