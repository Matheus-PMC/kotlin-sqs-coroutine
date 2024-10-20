package com.coroutine

import org.springframework.data.annotation.Id
import java.util.UUID

data class MessageEntity (
    @Id
    val id: String = UUID.randomUUID().toString(),
    val message: String
)