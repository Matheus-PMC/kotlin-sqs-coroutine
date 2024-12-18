package com.coroutine.adapter.out.data

import com.coroutine.adapter.out.repository.MessageRepository
import com.coroutine.application.port.MessageDataPort
import com.coroutine.extension.LoggableExtension
import com.coroutine.extension.MessageExtension
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class MessageData(
    private val messageExtension: MessageExtension,
    private val messageRepository: MessageRepository
) : MessageDataPort, LoggableExtension() {


    override suspend fun salvarMensagem(message: String) {
        for (i in 1..3) {
            delay(Random.nextLong(100L, 2000L))
            println("Iniciou for tentativa: $i, da msg: $message")
        }
        println("Test concluído para a mensagem: $message")


//        val messageEntity = MessageEntity(message = message)
//        messageRepository.save(messageEntity)
//            .doOnError { e ->
//                log.error(messageExtension.gerarLogsException("erro-salvar-bd-rds", message, e))
//            }
//            .subscribe().also { log.info(messageExtension.buscarMensagem("sucesso-salvar-bd-rds", message)) }
    }
}
