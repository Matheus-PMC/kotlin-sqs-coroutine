package com.coroutine.adapter.out.data

import com.coroutine.adapter.out.entity.MessageEntity
import com.coroutine.adapter.out.repository.MessageRepository
import com.coroutine.application.port.MessageDataPort
import com.coroutine.extension.LoggableExtension
import com.coroutine.extension.MessageExtension
import org.springframework.stereotype.Component

@Component
class MessageData(
    private val messageExtension: MessageExtension,
    private val messageRepository: MessageRepository
) : MessageDataPort, LoggableExtension() {


    override fun salvarMensagem(message: String) {
        val messageEntity = MessageEntity(message = message)
        messageRepository.save(messageEntity)
            .doOnError { e ->
                log.error(messageExtension.gerarLogsException("erro-salvar-bd-rds", message, e))
            }
            .subscribe().also { log.info(messageExtension.buscarMensagem("sucesso-salvar-bd-rds", message)) }
    }
}
