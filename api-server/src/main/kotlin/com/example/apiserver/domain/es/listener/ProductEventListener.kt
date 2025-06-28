package com.example.apiserver.domain.es.listener

import com.example.apiserver.domain.es.event.ProductCreateEvent
import com.example.core.domain.product.repository.ProductDocumentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductEventListener(
    private val productDocumentRepository: ProductDocumentRepository
) {

    private val logger = LoggerFactory.getLogger(ProductEventListener::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleProductCreateEvent(event: ProductCreateEvent) {
        logger.info("상품 저장 이벤트 수: ${event.productId}")

        val document = event.toDocument()

        productDocumentRepository.save(document)
    }

}