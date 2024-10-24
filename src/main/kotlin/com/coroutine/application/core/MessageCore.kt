package com.coroutine.application.core

import com.coroutine.application.port.MessageDataPort
import org.springframework.stereotype.Component

@Component
class MessageCore(
    private val messageDataPort: MessageDataPort
) {
    fun recebendoMensagem(message: String) {
        messageDataPort.salvarMensagem(message)
    }
}
