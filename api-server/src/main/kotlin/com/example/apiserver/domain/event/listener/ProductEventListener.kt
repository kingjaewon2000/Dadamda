package com.example.apiserver.domain.event.listener

import com.example.apiserver.domain.event.dto.ProductCreateEvent
import com.example.apiserver.domain.event.dto.ProductUpdateEvent
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.core.global.exception.ApiException
import com.example.core.global.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import kotlin.jvm.optionals.getOrNull

@Component
class ProductEventListener(
    private val productDocumentRepository: ProductDocumentRepository
) {

    private val logger = LoggerFactory.getLogger(ProductEventListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleProductCreateEvent(event: ProductCreateEvent) {
        logger.info("상품 저장 이벤트 수신 ID: ${event.productId}")

        val product = event.toDocument()

        productDocumentRepository.save(product)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleProductUpdateEvent(event: ProductUpdateEvent) {
        logger.info("상품 업데이트 이벤트 수신 ID: ${event.component1()}")

        val product = productDocumentRepository.findById(event.productId)
            .getOrNull() ?: throw ApiException(ErrorCode.PRODUCT_NOT_FOUND)

        product.updateInfo(
            name = event.name,
            price = event.price,
        )

        productDocumentRepository.save(product)
    }

}