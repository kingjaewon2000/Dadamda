package com.example.apiserver.domain.product.service

import com.example.apiserver.domain.event.dto.ProductCreateEvent
import com.example.core.domain.product.dto.ProductCreateRequest
import com.example.core.domain.product.dto.ProductIdResponse
import com.example.core.domain.product.dto.ProductResponse
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.core.domain.product.repository.ProductRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository,
    private val productDocumentRepository: ProductDocumentRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun getTop10Products(): List<ProductResponse> {
        val top10Products = productDocumentRepository.findTop10ByOrderBySalesCountDescIdDesc()

        return top10Products.map(ProductResponse::from)
    }

    @Transactional
    fun createProduct(request: ProductCreateRequest): ProductIdResponse {
        val product = productRepository.save(request.toEntity())

        val event = ProductCreateEvent(
            productId = product.productId,
            name = product.name,
            price = product.price,
            stockQuantity = product.stockQuantity,
            createdAt = product.createdAt
        )
        eventPublisher.publishEvent(event)

        return ProductIdResponse(product.productId)
    }

}