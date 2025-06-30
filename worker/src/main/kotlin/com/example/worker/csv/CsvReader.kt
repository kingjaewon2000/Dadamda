package com.example.worker.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path

@Component
class CsvReader {

    private val logger = LoggerFactory.getLogger(CsvReader::class.java)

    fun <T> readRecords(filePath: Path, rowMapper: (CSVRecord) -> T): Sequence<T> {
        if (Files.notExists(filePath)) {
            logger.info("처리할 CSV 파일이 존재하지 않습니다: ${filePath.fileName}")
            return emptySequence()
        }

        return sequence {
            val reader = Files.newBufferedReader(filePath, Charsets.UTF_8)
            val csvParser = CSVParser.parse(
                reader,
                CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get()
            )

            csvParser.use { parser ->
                for (record in parser.records) {
                    yield(rowMapper(record))
                }
            }
        }
    }

}