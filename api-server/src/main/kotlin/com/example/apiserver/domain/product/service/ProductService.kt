package com.example.apiserver.domain.product.service

import com.example.apiserver.domain.event.dto.ProductCreateEvent
import com.example.apiserver.domain.product.dto.ProductCreateRequest
import com.example.apiserver.domain.product.dto.ProductIdResponse
import com.example.apiserver.domain.product.dto.ProductUpdateRequest
import com.example.core.domain.product.dto.ProductResponse
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.core.domain.product.repository.ProductRepository
import com.example.core.global.exception.ApiException
import com.example.core.global.exception.ErrorCode
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

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

    @Transactional
    fun updateProduct(productId: Long, request: ProductUpdateRequest): ProductIdResponse {
        val product = productRepository.findById(productId).getOrNull() ?: throw ApiException(ErrorCode.PRODUCT_NOT_FOUND)

        product.updateInfo(
            name = request.name,
            price = request.price,
        )

        return ProductIdResponse(product.productId)
    }

}