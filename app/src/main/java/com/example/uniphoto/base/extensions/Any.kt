package com.example.uniphoto.base.extensions

import com.example.uniphoto.base.log.KLogger


val Any?.className get() = if (this != null) this::class.java.simpleName else "[null]"

fun Any.createLogger(
    debug: Boolean = false,
    messageFormatter: (Any?) -> String = KLogger.DEFAULT_FORMAT
) = KLogger(className, debug, messageFormatter)