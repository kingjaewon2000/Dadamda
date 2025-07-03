package com.example.worker.schedule

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.repository.ProductRepository
import com.example.worker.csv.CsvReader
import com.example.worker.csv.CsvWriter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.data.elasticsearch.NoSuchIndexException
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.IndexOperations
import org.springframework.data.elasticsearch.core.index.AliasAction
import org.springframework.data.elasticsearch.core.index.AliasActionParameters
import org.springframework.data.elasticsearch.core.index.AliasActions
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.stream.Stream

@Component
class ProductSchedule(
    private val csvReader: CsvReader,
    private val csvWriter: CsvWriter,
    private val productRepository: ProductRepository,
    private val elasticsearchOperations: ElasticsearchOperations,
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(ProductSchedule::class.java)

    @Value("\${csv.export-path}")
    private lateinit var exportPath: String

    companion object {
        private const val PRODUCT_INDEX_NAME = "product"
        private const val PRODUCT_ALIAS = "product_alias"
        private const val CHUNK_SIZE = 1000
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional(readOnly = true)
    fun runFullIndexingJob() {
        logger.info("판매량 업데이트 스케줄 시작")

        val currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)

        val newIndexName =
            "${PRODUCT_INDEX_NAME}_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"))}"
        val newIndex = IndexCoordinates.of(newIndexName)
        val newIndexOps = elasticsearchOperations.indexOps(newIndex)
        val productIndexOps = elasticsearchOperations.indexOps(ProductDocument::class.java)

        if (!newIndexOps.exists()) {
            newIndexOps.create(getSettings())
            newIndexOps.putMapping(productIndexOps.createMapping())
            logger.info("새로운 인덱스 생성 및 매칭 완료: {}", newIndexName)
        }

        val productStream = productRepository.findAllWithSalesCountAsStream()

        val fileName = "products_${currentTime}.csv"
        val filePath = Paths.get(exportPath, fileName)

        // 상품 데이터를 CSV 파일로 저장
        exportProductsToCsvFile(filePath, productStream)

        // 상품 데이터를 CSV로 부터 읽어 새로운 인덱스에 인덱싱 처리
        reIndexingElasticsearchFromCsv(newIndex, filePath)

        // 상품 별칭을 새로운 인덱스로 교체
        changeAlias(productIndexOps, newIndexName)
    }

    private fun getSettings(): Map<String, Any> {
        val resource = resourceLoader.getResource("classpath:elasticsearch/settings/product.json")
        val settings: Map<String, Any> = resource.inputStream.use {
            objectMapper.readValue(it, object : TypeReference<Map<String, Any>>() {})
        }

        return settings
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

    private fun reIndexingElasticsearchFromCsv(index: IndexCoordinates, filePath: Path) {
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
                    elasticsearchOperations.save(chunk, index)
                }
            }

            logger.info("CSV 파일로부터의 재색인 작업을 완료했습니다.")
        } catch (e: Exception) {
            logger.error("CSV 재색인 중 오류 발생: ${filePath.fileName}", e)
            throw e
        }
    }

    private fun changeAlias(indexOps: IndexOperations, newIndexName: String) {
        val oldIndexNames: Set<String> = try {
            indexOps.getAliasesForIndex(PRODUCT_ALIAS).keys
        } catch (e: NoSuchIndexException) {
            emptySet()
        }

        val addAction = AliasAction.Add(
            AliasActionParameters.builder()
                .withIndices(newIndexName)
                .withAliases(PRODUCT_ALIAS)
                .build()
        )

        val removeActions = oldIndexNames
            .filter { it != newIndexName }
            .map { oldIndex ->
                AliasAction.Remove(
                    AliasActionParameters.builder()
                        .withIndices(oldIndex)
                        .withAliases(PRODUCT_ALIAS)
                        .build()
                )
            }

        val actions = removeActions + addAction
        indexOps.alias(AliasActions(*actions.toTypedArray()))

        if (oldIndexNames.isEmpty()) {
            logger.info("별칭 추가 완료: {} -> {}", PRODUCT_ALIAS, newIndexName)
        } else {
            logger.info("별칭 교체 완료: {} -> {}", PRODUCT_ALIAS, newIndexName)
        }
    }

}