package com.example.core.domain.log.repository

import com.example.core.domain.log.entity.Log

interface JdbcLogRepository {

    fun bulkInsert(logs: List<Log>)

}
