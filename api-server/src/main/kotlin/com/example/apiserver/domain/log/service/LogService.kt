package com.example.apiserver.domain.log.service

import com.example.apiserver.domain.log.dto.LogIdResponse
import com.example.apiserver.domain.log.entity.Log
import com.example.apiserver.domain.log.repository.LogRepository
import com.example.apiserver.domain.search.dto.ProductSortBy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LogService(
    private val logRepository: LogRepository
) {

    @Transactional
    fun log(sortBy: ProductSortBy, keyword: String): LogIdResponse {
        val log = logRepository.save(
            Log(
                sortBy = sortBy,
                keyword = keyword,
                length = keyword.length,
            )
        )

        return LogIdResponse(log.logId)
    }

}