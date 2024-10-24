package com.coroutine.extension

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class MessageExtension(@Autowired private val messageSource: MessageSource) {

    private val messages: Properties = Properties()

    init {
        val resource = this::class.java.getResourceAsStream("/siglas.properties")
        resource?.use { messages.load(it) }
    }

    fun gerarLogsException(name: String, msg: String? = null, exception: Throwable): String {
        val parts = mutableListOf<String>()
        val mensagem = buscarMensagem(name, msg)
        val sigla = buscarSigla(mensagem)

        if (sigla != null) parts.add("sigla: [$sigla]")
        if (mensagem != null) parts.add("mensagem: [$mensagem]")
        if (exception.message != null) parts.add("exception: [${exception.message}]")

        return parts.joinToString(", ")
    }

    fun buscarMensagem(name: String, msg: String? = null): String? {
        val args = if (msg != null) arrayOf(msg) else emptyArray()
        return messageSource.getMessage(name, args, Locale.getDefault())
    }

    fun buscarSigla(message: String?): String? {
        return if (message != null)
            messages.entries
                .firstOrNull { isSimilar(it.value.toString(), message) }
                ?.key
                ?.toString()
        else null
    }

    private fun isSimilar(value1: String, value2: String, threshold: Double = 0.7): Boolean {
        val dp = Array(value1.length + 1) { IntArray(value2.length + 1) }
        for (i in 0..value1.length) dp[i][0] = i
        for (j in 0..value2.length) dp[0][j] = j

        for (i in 1..value1.length) {
            for (j in 1..value2.length) {
                val cost = if (value1[i - 1] == value2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        val distance = dp[value1.length][value2.length]
        val maxLength = maxOf(value1.length, value2.length)
        return (1.0 - distance.toDouble() / maxLength) >= threshold
    }
}
