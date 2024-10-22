package com.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Component


@Component
class SQSConsumer(
    private val coroutineScope: CoroutineScope,
    private val messageRepository: MessageRepository
) : Loggable() {

    @SqsListener("sample-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    fun receiveMessage(message: String) {
        coroutineScope.launch {
            runCatching {
                log.info("SQS Message Received")
            }.onSuccess {
                log.info("SQS Message Received com Sucesso! : {}", message)
                val messageEntity = MessageEntity(message = message)

                messageRepository.save(messageEntity)
                    .doOnError { e -> log.error("Erro ao salvar a mensagem: {}", e.message) }
                    .subscribe()
                    .also { log.info("Salvo com sucesso com Sucesso! : {}", message) }

            }.onFailure { exception ->
                log.error("SQS Message Received com Erro! : {}", exception.message)
            }
        }
    }
}