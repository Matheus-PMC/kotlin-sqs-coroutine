package com.coroutine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs

@SpringBootApplication
class CoroutineApplication

fun main(args: Array<String>) {
	runApplication<CoroutineApplication>(*args)
}
