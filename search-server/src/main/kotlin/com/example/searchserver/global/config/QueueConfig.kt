package com.example.searchserver.global.config

import com.example.core.domain.log.entity.Log
import com.example.searchserver.domain.queue.ConcurrentLogQueue
import com.example.searchserver.domain.queue.Consumer
import com.example.searchserver.domain.queue.Producer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfig {

    @Bean
    fun queue(): ConcurrentLogQueue<Log> {
        return ConcurrentLogQueue(batchSize = 1000, timeoutMillis = 10000L)
    }

    @Bean
    fun producer(queue: ConcurrentLogQueue<Log>): Producer<Log> = queue

    @Bean
    fun consumer(queue: ConcurrentLogQueue<Log>): Consumer<Log> = queue

}