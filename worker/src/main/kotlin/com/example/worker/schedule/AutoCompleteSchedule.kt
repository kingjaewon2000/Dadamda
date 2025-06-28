package com.example.worker.schedule

import com.example.core.domain.autocomplete.repository.AutoCompleteRepository
import com.example.core.domain.log.entity.Log
import com.example.core.domain.log.repository.LogRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AutoCompleteSchedule(
    private val logRepository: LogRepository,
    private val autocompleteRepository: AutoCompleteRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(AutoCompleteSchedule::class.java)

    companion object {
        private const val WINDOW_MINUTES = 1L
        private const val MIN_KEYWORD_LENGTH = 1
        private const val MAX_KEYWORD_LENGTH = 49
    }

    @Scheduled(cron = "0 * * * * *")
    fun updateAutocompleteKeywords() {
        logger.info("검색어 자동완성 업데이트 스케줄 시작")

        val endTime = LocalDateTime.now()
        val startTime = endTime.minusMinutes(WINDOW_MINUTES)

        val logs = findLogsByPeriod(startTime, endTime)
        if (logs.isEmpty()) {
            logger.info("자동완성으로 갱신할 로그가 없습니다.")
            return
        }

        val frequencyMap = calculateKeywordFrequencies(logs)
        if (frequencyMap.isEmpty()) {
            logger.info("유효한 키워드가 없어 자동완성 갱신을 건너뜁니다.")
            return
        }

        updateKeywords(frequencyMap)
    }

    private fun findLogsByPeriod(startTime: LocalDateTime, endTime: LocalDateTime): List<Log> {
        return logRepository.findByLoggedAtBetween(startTime, endTime)
    }

    private fun calculateKeywordFrequencies(logs: List<Log>): Map<String, Long> {
        return logs
            .asSequence()
            .map { it.keyword.trim().lowercase() }
            .filter { it.length in MIN_KEYWORD_LENGTH..MAX_KEYWORD_LENGTH }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
    }

    private fun updateKeywords(frequencyMap: Map<String, Long>) {
        try {
            autocompleteRepository.bulkUpdateFrequency(frequencyMap)
            logger.info("자동완성 키워드 {}건을 성공적으로 업데이트 했습니다.", frequencyMap.size)
        } catch (e: Exception) {
            logger.error("자동완성 키워드 업데이트 중 오류 발생", e)
        }
    }

}