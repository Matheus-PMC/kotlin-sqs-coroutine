package com.coroutine.adapter.out.entity

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("\"table-message\"")
data class MessageEntity(
    @Id
    @Column("id")
    private var _id: String? = null, // Propriedade privada para evitar conflitos
    @Column("message")
    val message: String
) : Persistable<String> {

    override fun getId(): String = _id ?: UUID.randomUUID().toString()

    override fun isNew(): Boolean = _id == null
}