package com.example.worker.schedule

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.order.repository.OrderRepository
import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.core.domain.product.repository.ProductRepository
import com.example.worker.csv.CsvWriter
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

@Component
class ProductSchedule(
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
    fun fullIndexing() {
        logger.info("판매량 업데이트 스케줄 시작")

        val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)

        val productStream = productRepository.findAllWithSalesCountAsStream()

        val fileName = "products_${currentTime}.csv"
        val filePath = Paths.get(exportPath, fileName)

        // 상품 데이터를 CSV 파일로 저장
        createProductsToCsv(filePath, productStream)

        reIndexingElasticsearchFromCsv(filePath)

    }

    private fun createProductsToCsv(filePath: Path, productStream: Stream<ProductSalesResponse>) {
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
        if (Files.notExists(filePath)) {
            logger.warn("처리할 CSV 파일이 존재하지 않습니다: ${filePath.fileName}")
            return
        }
        val format = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get()

        val reader = Files.newBufferedReader(filePath, Charsets.UTF_8)
        val csvParser = CSVParser.parse(reader, format)
        val products = mutableListOf<ProductDocument>()
        var totalProcessedLines = 0L

        try {
            csvParser.use { parser ->
                for (record in parser.records) {
                    val productId = record["product_id"].toLong()
                    val name = record["name"]
                    val price = record["price"].toInt()
                    val stockQuantity = record["stock_quantity"].toInt()
                    val salesCount = record["sales_count"].toLong()
                    val createdAt = LocalDateTime.parse(record["created_at"])

                    val productDocument = ProductDocument(
                        id = productId,
                        name = name,
                        price = price,
                        stockQuantity = stockQuantity,
                        salesCount = salesCount,
                        createdAt = createdAt,
                    )
                    products.add(productDocument)
                    totalProcessedLines++

                    if (products.size >= CHUNK_SIZE) {
                        productDocumentRepository.saveAll(products)
                        products.clear()
                    }
                }
            }
            logger.info("총 $totalProcessedLines 라인 업데이트 완료.")
        } catch (e: Exception) {
            logger.error("CSV 처리 중 오류 발생: ${filePath.fileName}", e)
        }

    }

}