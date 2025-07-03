package com.example.worker.schedule

import com.example.core.domain.autocomplete.repository.AutoCompleteRepository
import com.example.core.domain.log.entity.Log
import com.example.core.domain.log.repository.LogRepository
import com.example.worker.csv.CsvReader
import com.example.worker.csv.CsvWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

@Component
class AutoCompleteSchedule(
    private val csvReader: CsvReader,
    private val csvWriter: CsvWriter,
    private val logRepository: LogRepository,
    private val autocompleteRepository: AutoCompleteRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(AutoCompleteSchedule::class.java)

    @Value("\${csv.export-path}")
    private lateinit var exportPath: String

    companion object {
        private const val CHUNK_SIZE = 1000
        private val KST_ZONE_ID = ZoneId.of("Asia/Seoul").toString()
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    fun runAutocompleteUpdateJob() {
        logger.info("검색어 자동완성 업데이트 스케줄 시작")

        val now = LocalDateTime.now(ZoneId.of(KST_ZONE_ID))
        val endTime = now.truncatedTo(ChronoUnit.MINUTES)
        val startTime = now.minusMinutes(1)

        logger.info("데이터 조회 기간 (KST 기준): $startTime ~ $endTime")

        val logStream = logRepository.findByLoggedAtBetweenAsStream(startTime, endTime)

        val fileName = "logs_${startTime}_${endTime}.csv"
        val filePath = Paths.get(exportPath, fileName)

        // 로그 데이터를 CSV 파일로 작성
        exportLogsToCsvFile(filePath, logStream)

        // 청크 단위로 CSV 파일을 읽어 엘라스틱 서치에 업데이트
        updateElasticsearchFromCsv(filePath)
    }

    private fun exportLogsToCsvFile(filePath: Path, logStream: Stream<Log>) {
        csvWriter.write(
            filePath = filePath,
            dataStream = logStream,
            csvHeaders = arrayOf("log_id", "sort_option", "keyword", "length", "logged_at"),
            dataExtractor = {
                listOf(it.logId, it.sortOption.name, it.keyword, it.length, it.loggedAt)
            }
        )
    }

    private fun updateElasticsearchFromCsv(filePath: Path) {
        logger.info("CSV 파일로부터 엘라스틱서치 재색인을 시작합니다: ${filePath.fileName}")

        try {
            val sequence = csvReader.readRecords(filePath) { record ->
                record["keyword"].trim().lowercase()
            }

            sequence.chunked(CHUNK_SIZE).forEach { chunk ->
                if (chunk.isNotEmpty()) {
                    val frequencyMap = calculateFrequencyMap(chunk)
                    updateElasticsearch(frequencyMap)
                }
            }

            logger.info("CSV 파일로부터의 재색인 작업을 완료했습니다.")
        } catch (e: Exception) {
            logger.error("CSV 재색인 중 오류 발생: ${filePath.fileName}", e)
            throw e
        }
    }

    private fun calculateFrequencyMap(keywords: List<String>): Map<String, Long> {
        return keywords.asSequence()
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
    }

    private fun updateElasticsearch(frequencyMap: Map<String, Long>) {
        try {
            autocompleteRepository.bulkUpdateFrequency(frequencyMap)
        } catch (e: Exception) {
            logger.error("자동완성 키워드 업데이트 중 오류 발생", e)
        }
    }

}