package com.example.apiserver.domain.log.repository

import com.example.apiserver.domain.log.entity.Log
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.Timestamp

@Repository
class LogRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : LogRepository {

    companion object {
        private const val BULK_INSERT_SQL = """
            INSERT INTO logs (keyword, length, sort_by, logged_at) 
            VALUES (?, ?, ?, ?)
        """
    }

    override fun bulkInsert(logs: List<Log>) {
        if (logs.isEmpty()) return

        jdbcTemplate.batchUpdate(
            BULK_INSERT_SQL,
            logs,
            logs.size,
            extractedPreparedStatement()
        )
    }

    private fun extractedPreparedStatement(): (PreparedStatement, Log) -> Unit = { ps: PreparedStatement, log: Log ->
        ps.setString(1, log.keyword)
        ps.setInt(2, log.length)
        ps.setString(3, log.sortBy.name)
        ps.setTimestamp(4, Timestamp.valueOf(log.loggedAt))
    }

}