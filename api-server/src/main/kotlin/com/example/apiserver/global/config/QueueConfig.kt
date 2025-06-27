package com.example.apiserver.global.config

import com.example.core.domain.log.entity.Log
import com.example.apiserver.domain.queue.BlockingLogQueue
import com.example.apiserver.domain.queue.Consumer
import com.example.apiserver.domain.queue.Producer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfig {

    @Bean
    fun queue(): BlockingLogQueue<Log> {
        return BlockingLogQueue(batchSize = 100, timeoutMillis = 10000L)
    }

    @Bean
    fun producer(queue: BlockingLogQueue<Log>): Producer<Log> = queue

    @Bean
    fun consumer(queue: BlockingLogQueue<Log>): Consumer<Log> = queue

}