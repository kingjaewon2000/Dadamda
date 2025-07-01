package com.example.core.domain.log.repository

import com.example.core.domain.log.entity.Log
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.stream.Stream

interface LogRepository : JpaRepository<Log, Long>, JdbcLogRepository {

    @Query("SELECT l FROM Log l where l.loggedAt between :startTime and :endTime")
    fun findByLoggedAtBetweenAsStream(startTime: LocalDateTime, endTime: LocalDateTime): Stream<Log>

}