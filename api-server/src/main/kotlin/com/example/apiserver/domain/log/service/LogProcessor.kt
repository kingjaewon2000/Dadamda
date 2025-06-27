package com.example.apiserver.domain.log.service

import com.example.apiserver.domain.queue.Consumer
import com.example.core.domain.log.entity.Log
import com.example.core.domain.log.repository.LogRepository
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class LogProcessor(
    @Qualifier("processExecutor") private val executor: ThreadPoolTaskExecutor,
    private val consumer: Consumer<Log>,
    private val logRepository: LogRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(LogProcessor::class.java)

    @PostConstruct
    fun init() {
        repeat(executor.maxPoolSize) {
            executor.submit { processQueue() }
        }
    }

    private fun processQueue() {
        while (true) {
            val logs: List<Log> = consumer.consume()
            logger.info("[{}]: {}개의 로그 컨슘 완료", Thread.currentThread().name, logs.size)

            if (logs.isNotEmpty()) {
                try {
                    logRepository.bulkInsert(logs)
                    logger.info("[{}]: {}개의 로그 배치 Insert 완료", Thread.currentThread().name, logs.size)
                } catch (e: Exception) {
                    logger.error("[{}]: {}", Thread.currentThread().name, e.message)
                }
            }
        }
    }

}