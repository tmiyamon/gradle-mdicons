package com.tmiyamon.mdicons

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class L {
    static final Logger logger = LoggerFactory.getLogger("gradle-mdicons")

    static d(String msg) {
        logger.debug(msg)
    }
    static i(String msg) {
        logger.info(msg)
    }
    static w(String msg) {
        logger.warn(msg)
    }
    static e(String msg) {
        logger.error(msg)
    }
}
