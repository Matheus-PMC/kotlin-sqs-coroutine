package com.coroutine

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableRabbit
class CoroutineApplication

fun main(args: Array<String>) {
    runApplication<CoroutineApplication>(*args)
}
