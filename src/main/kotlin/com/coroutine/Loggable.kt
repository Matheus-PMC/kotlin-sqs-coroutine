package com.coroutine

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Loggable {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)
}