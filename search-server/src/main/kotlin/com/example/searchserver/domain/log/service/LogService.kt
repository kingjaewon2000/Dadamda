package com.example.searchserver.domain.log.service

import com.example.core.domain.log.entity.Log
import com.example.core.domain.log.entity.SortOption
import com.example.searchserver.domain.queue.Producer
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class LogService(
    private val producer: Producer<Log>
) {

    @Async("produceExecutor")
    fun log(sortOption: SortOption, keyword: String) {
        producer.produce(
            Log(
                sortOption = sortOption,
                keyword = keyword,
                length = keyword.length
            )
        )
    }

}