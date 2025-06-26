package com.example.core.domain.log.repository

import com.example.core.domain.log.entity.Log
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long>, JdbcLogRepository

