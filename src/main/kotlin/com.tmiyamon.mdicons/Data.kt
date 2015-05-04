package com.tmiyamon.mdicons

import java.io.Serializable

data class Result(val srcPath: String, val dstPath: String, val taskName: String)
data class Asset(val names: Array<String>, val colors: Array<String>, val sizes: Array<String>): Serializable

