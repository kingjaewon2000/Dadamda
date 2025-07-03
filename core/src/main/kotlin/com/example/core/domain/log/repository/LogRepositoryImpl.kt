package com.example.core.domain.log.repository

import com.example.core.domain.log.entity.Log
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime

class LogRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : JdbcLogRepository {

    companion object {
        private const val BULK_INSERT_SQL = """
            INSERT INTO logs (keyword, length, sort_option, logged_at) 
            VALUES (?, ?, ?, ?)
        """

        private val KST_ZONE_ID = ZoneId.of("Asia/Seoul")
    }

    override fun bulkInsert(logs: List<Log>) {
        if (logs.isEmpty()) return

        val nowInKst = ZonedDateTime.now(KST_ZONE_ID)
        val kstTimestamp = Timestamp.from(nowInKst.toInstant())

        jdbcTemplate.batchUpdate(
            BULK_INSERT_SQL,
            logs,
            logs.size,
            extractedPreparedStatement(kstTimestamp)
        )
    }

    private fun extractedPreparedStatement(kstTimestamp: Timestamp): (PreparedStatement, Log) -> Unit =
        { ps: PreparedStatement, log: Log ->
            ps.setString(1, log.keyword)
            ps.setInt(2, log.length)
            ps.setString(3, log.sortOption.name)
            ps.setTimestamp(4, kstTimestamp)
        }

}