package com.coroutine.adapter.`in`

import com.coroutine.extension.LoggableExtension
import com.coroutine.adapter.out.entity.MessageEntity
import com.coroutine.adapter.out.repository.MessageRepository
import com.coroutine.application.core.MessageCore
import com.coroutine.extension.MessageExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Component


@Component
class SQSConsumer(
    private val messageExtension: MessageExtension,
    private val coroutineScope: CoroutineScope,
    private val messageCore: MessageCore
) : LoggableExtension() {

    @SqsListener("sample-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    fun recebendoMensagemSQS(message: String) {
        coroutineScope.launch {
            runCatching {
                log.info(messageExtension.buscarMensagem("sqs-mensagem", message))
                messageCore.recebendoMensagem(message)
            }.onSuccess {
                log.info(messageExtension.buscarMensagem("mensagem-finalizada-com-sucesso", message))
            }.onFailure { e ->
                log.error(messageExtension.gerarLogsException("mensagem-finalizada-com-erro", message, e))
            }
        }
    }
}