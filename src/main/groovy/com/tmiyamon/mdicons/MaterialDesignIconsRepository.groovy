package com.tmiyamon.mdicons

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by tmiyamon on 4/26/15.
 */
class MaterialDesignIconsRepository {
    private static final Logger logger = LoggerFactory.getLogger(MaterialDesignIconsRepository.canonicalName)
    public static final String URL = 'git@github.com:google/material-design-icons.git'

    def cloneTo(File dst) {
        def sout = new StringBuilder()
        def serr = new StringBuilder()
        def p = "git clone ${URL} ${dst.absolutePath}".execute()
        p.consumeProcessOutput(sout, serr)

        def ret = p.waitFor()
        if (ret != 0) {
            logger.warn(serr.toString())
        }
    }
}
