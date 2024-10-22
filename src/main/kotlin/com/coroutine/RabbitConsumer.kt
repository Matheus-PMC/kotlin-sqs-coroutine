package com.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class RabbitConsumer(
    private val coroutineScope: CoroutineScope,
    private val messageRepository: MessageRepository
) : Loggable() {

    @RabbitListener(queues = ["sample-queue"])
    fun receiveMessage(@Payload message: String) {
        coroutineScope.launch {
            runCatching {
                log.info("Rabbit Message Received")
            }.onSuccess {
                log.info("Rabbit Message Received com Sucesso! : {}", message)

                val messageEntity = MessageEntity(message = message)

                messageRepository.save(messageEntity)
                    .doOnError { e -> log.error("Erro ao salvar a mensagem: {}", e.message) }
                    .subscribe()
                    .also { log.info("Salvo com sucesso com Sucesso! : {}", message) }

                val test: List<MessageEntity> = messageRepository.findAll().toStream().toList()
                println(test)
            }.onFailure { exception ->
                log.error("Rabbit Message Received com Erro! : {}", exception.message)
            }
        }
    }
}