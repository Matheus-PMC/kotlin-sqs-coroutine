package com.coroutine.extension

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class LoggableExtension {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)
}
