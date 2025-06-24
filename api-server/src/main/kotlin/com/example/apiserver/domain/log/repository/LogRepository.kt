package com.example.apiserver.domain.log.repository

import com.example.apiserver.domain.log.entity.Log
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long>