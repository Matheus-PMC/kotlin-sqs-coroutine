package com.coroutine.adapter.`in`

import com.coroutine.extension.LoggableExtension
import com.coroutine.adapter.out.entity.MessageEntity
import com.coroutine.application.core.MessageCore
import com.coroutine.extension.MessageExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class RabbitConsumer(
    private val messageExtension: MessageExtension,
    private val coroutineScope: CoroutineScope,
    private val messageCore: MessageCore
) : LoggableExtension() {

    @RabbitListener(queues = ["sample-queue"])
    fun recebendoMensagemMQ(@Payload message: String) {
        coroutineScope.launch {
            runCatching {
                log.info(messageExtension.buscarMensagem("rabbit-mensagem", message))
                messageCore.recebendoMensagem(message)
            }.onSuccess {
                log.info(messageExtension.buscarMensagem("mensagem-finalizada-com-sucesso", message))
            }.onFailure { e ->
                log.error(messageExtension.gerarLogsException("mensagem-finalizada-com-erro", message, e))
            }
        }
    }
}