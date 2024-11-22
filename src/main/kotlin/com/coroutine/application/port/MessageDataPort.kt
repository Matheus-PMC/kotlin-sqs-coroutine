package com.coroutine.application.port

interface MessageDataPort {
    suspend fun salvarMensagem(message: String)
}
