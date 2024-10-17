package com.coroutine

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
class Configuration(
    @Value("\${cloud.aws.endpoint.uri}") private var host: String,
    @Value("\${cloud.aws.credentials.access-key}") private var accessKeyId: String,
    @Value("\${cloud.aws.credentials.secret-key}") private var secretAccessKey: String,
    @Value("\${cloud.aws.region.static}") private var region: String
) {

    @Bean
    @Primary
    fun amazonSQSAsync(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder.standard()
            .withEndpointConfiguration(getEndpointConfiguration(host, region))
            .withCredentials(getCredentialsProvider(accessKeyId, secretAccessKey))
            .build()
    }

    @Bean
    fun amazonSNSAsync(): AmazonSNS {
        return AmazonSNSAsyncClientBuilder.standard()
            .withEndpointConfiguration(getEndpointConfiguration(host, region))
            .withCredentials(getCredentialsProvider(accessKeyId, secretAccessKey))
            .build()
    }

    @Bean
    fun queueMessagingTemplate(): QueueMessagingTemplate {
        return QueueMessagingTemplate(amazonSQSAsync())
    }

    @Bean
    fun queueMessageHandler(): QueueMessageHandler {
        val queueMessageHandlerFactory = QueueMessageHandlerFactory()
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync())
        return queueMessageHandlerFactory.createQueueMessageHandler()
    }

    @Bean
    fun simpleMessageListenerContainer(): SimpleMessageListenerContainer {
        val simpleListenerContainer = SimpleMessageListenerContainer()
        simpleListenerContainer.setAmazonSqs(amazonSQSAsync())
        simpleListenerContainer.setMessageHandler(queueMessageHandler())
        return simpleListenerContainer
    }

    @Bean
    fun coroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    companion object {
        fun getEndpointConfiguration(host: String, region: String): AwsClientBuilder.EndpointConfiguration {
            return AwsClientBuilder.EndpointConfiguration(host, region)
        }

        fun getCredentialsProvider(accessKeyId: String, secretAccessKey: String): AWSStaticCredentialsProvider {
            return AWSStaticCredentialsProvider(BasicAWSCredentials(accessKeyId, secretAccessKey))
        }
    }
}
