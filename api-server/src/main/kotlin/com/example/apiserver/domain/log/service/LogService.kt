package com.example.apiserver.domain.log.service

import com.example.apiserver.domain.log.entity.Log
import com.example.apiserver.domain.queue.Producer
import com.example.apiserver.domain.search.dto.ProductSortBy
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LogService(
    private val producer: Producer<Log>
) {

    @Async("produceExecutor")
    fun log(sortBy: ProductSortBy, keyword: String) {
        producer.produce(
            Log(
                sortBy = sortBy,
                keyword = keyword,
                length = keyword.length,
                loggedAt = LocalDateTime.now(),
            )
        )
    }

}