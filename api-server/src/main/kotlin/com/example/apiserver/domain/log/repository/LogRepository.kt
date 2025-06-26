package com.example.apiserver.domain.log.repository

import com.example.apiserver.domain.log.entity.Log

interface LogRepository {

    fun bulkInsert(logs: List<Log>)

}