package com.coroutine.adapter.`in`

import com.coroutine.application.core.MessageCore
import com.coroutine.extension.LoggableExtension
import com.coroutine.extension.MessageExtension
import kotlinx.coroutines.*
import org.springframework.cloud.aws.messaging.listener.Acknowledgment
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener
import org.springframework.stereotype.Component


@Component
class SQSConsumer(
    private val messageExtension: MessageExtension,
    private val coroutineScope: CoroutineScope,
    private val messageCore: MessageCore
) : LoggableExtension() {

    @SqsListener("sample-queue", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    fun recebendoMensagemSQS(message: String, acknowledgment: Acknowledgment) = coroutineScope.launch {
        log.info("Entrou: $message")
        runCatching {
            withContext(Dispatchers.IO) {
                messageCore.recebendoMensagem(message)
            }
        }.onSuccess {
            log.info("mensagem finalizada com sucesso: $message")
            acknowledgment.acknowledge()
            log.info("Acknowledge: $message")
        }.onFailure {
            log.error("mensagem finalizada com erro: $message")
        }.also { println("MDC CLEAR: $message") }
        println("Finalizou Fluxo completo! $message")
    }.ensureActive()
}