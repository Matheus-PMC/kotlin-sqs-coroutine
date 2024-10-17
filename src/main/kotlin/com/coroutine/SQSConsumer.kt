package com.coroutine

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Component


@Component
class SQSConsumer {

    @SqsListener("sample-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    fun receiveMessage(message: String) {
        LOGGER.info("SQS Message Received : {}", message)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(SQSConsumer::class.java)
    }
}