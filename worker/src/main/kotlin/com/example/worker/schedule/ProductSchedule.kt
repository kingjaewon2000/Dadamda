package com.example.worker.schedule

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.core.domain.product.repository.ProductRepository
import com.example.worker.csv.CsvReader
import com.example.worker.csv.CsvWriter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

@Component
class ProductSchedule(
    private val csvReader: CsvReader,
    private val csvWriter: CsvWriter,
    private val productRepository: ProductRepository,
    private val productDocumentRepository: ProductDocumentRepository,
) {

    private val logger = LoggerFactory.getLogger(ProductSchedule::class.java)

    @Value("\${csv.export-path}")
    private lateinit var exportPath: String

    private val CHUNK_SIZE = 1000

    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    fun runFullIndexingJob() {
        logger.info("판매량 업데이트 스케줄 시작")

        val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)

        val productStream = productRepository.findAllWithSalesCountAsStream()

        val fileName = "products_${currentTime}.csv"
        val filePath = Paths.get(exportPath, fileName)

        // 상품 데이터를 CSV 파일로 저장
        exportProductsToCsvFile(filePath, productStream)

        reIndexingElasticsearchFromCsv(filePath)

    }

    private fun exportProductsToCsvFile(filePath: Path, productStream: Stream<ProductSalesResponse>) {
        csvWriter.write(
            filePath = filePath,
            dataStream = productStream,
            csvHeaders = arrayOf("product_id", "name", "price", "stock_quantity", "sales_count", "created_at"),
            dataExtractor = {
                listOf(it.productId, it.name, it.price, it.stockQuantity, it.salesCount, it.createdAt)
            }
        )
    }

    private fun reIndexingElasticsearchFromCsv(filePath: Path) {
        logger.info("CSV 파일로부터 엘라스틱서치 재색인을 시작합니다: ${filePath.fileName}")

        try {
            val sequence = csvReader.readRecords(filePath) { record ->
                ProductDocument(
                    id = record["product_id"].toLong(),
                    name = record["name"],
                    price = record["price"].toInt(),
                    stockQuantity = record["stock_quantity"].toInt(),
                    salesCount = record["sales_count"].toLong(),
                    createdAt = LocalDateTime.parse(record["created_at"])
                )
            }

            sequence.chunked(CHUNK_SIZE).forEach { chunk ->
                if (chunk.isNotEmpty()) {
                    productDocumentRepository.saveAll(chunk)
                }
            }

            logger.info("CSV 파일로부터의 재색인 작업을 완료했습니다.")
        } catch (e: Exception) {
            logger.error("CSV 재색인 중 오류 발생: ${filePath.fileName}", e)
            throw e
        }
    }

}