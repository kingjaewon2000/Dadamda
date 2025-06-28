package com.example.searchserver.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean("produceExecutor")
    fun produceExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()

        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.queueCapacity = Int.MAX_VALUE
        executor.initialize()

        return executor
    }

    @Bean("processExecutor")
    fun processExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()

        executor.corePoolSize = 5
        executor.maxPoolSize = 5
        executor.queueCapacity = 0
        executor.initialize()

        return executor
    }

}