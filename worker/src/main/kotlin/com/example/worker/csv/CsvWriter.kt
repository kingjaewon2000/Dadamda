package com.example.worker.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream
import kotlin.use

@Component
class CsvWriter {

    private val logger: Logger = LoggerFactory.getLogger(CsvWriter::class.java)

    /**
     * 제네릭 스트림 데이터를 받아 CSV 파일로 생성합니다.
     *
     * @param T 데이터 스트림의 타입
     * @param filePath 생성할 CSV 파일의 전체 경로
     * @param dataStream CSV로 변환할 데이터 스트림
     * @param csvHeaders CSV 파일의 헤더
     * @param dataExtractor 데이터 객체(T)를 CSV 레코드(List<Any?>)로 변환하는 함수
     */
    fun <T> write(
        filePath: Path,
        dataStream: Stream<T>,
        csvHeaders: Array<String>,
        dataExtractor: (T) -> List<Any?>
    ) {
        val directory = filePath.parent

        try {
            if (Files.notExists(directory)) {
                Files.createDirectories(directory)
                logger.info("Created directory: $directory")
            }

            val format = CSVFormat.DEFAULT.builder().setHeader(*csvHeaders).get()

            FileWriter(filePath.toFile(), Charsets.UTF_8).use { fileWriter ->
                CSVPrinter(fileWriter, format).use { csvPrinter ->
                    dataStream.use { stream ->
                        logger.info("CSV 파일 쓰기를 시작합니다: $filePath")
                        var recordCount = 0
                        stream.forEach { item ->
                            val record = dataExtractor(item)
                            csvPrinter.printRecord(record)
                            recordCount++
                        }
                        logger.info("총 $recordCount 개의 레코드를 CSV 파일에 썼습니다.")
                    }
                }
            }
            logger.info("CSV 파일 생성을 완료했습니다: $filePath")
        } catch (e: Exception) {
            logger.error("CSV 파일 생성 중 오류가 발생했습니다: $filePath", e)
            Files.deleteIfExists(filePath)
            throw e
        }
    }

}