package com.coroutine.adapter.out.repository

import com.coroutine.adapter.out.entity.MessageEntity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : R2dbcRepository<MessageEntity, String> {
}
