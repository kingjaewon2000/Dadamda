package com.example.core.domain.log.repository

import com.example.core.domain.log.entity.Log
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement
import java.sql.Timestamp

class LogRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : JdbcLogRepository {

    companion object {
        private const val BULK_INSERT_SQL = """
            INSERT INTO logs (keyword, length, sort_by, logged_at) 
            VALUES (?, ?, ?, NOW())
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
    }

}