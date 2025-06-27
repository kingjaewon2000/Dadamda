package com.example.core.domain.log.repository

import com.example.core.domain.log.entity.Log
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface LogRepository : JpaRepository<Log, Long>, JdbcLogRepository {

    fun findByLoggedAtBetween(start: LocalDateTime, end: LocalDateTime): List<Log>

}