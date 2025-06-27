package com.example.apiserver.domain.product.service

import com.example.apiserver.domain.es.event.ProductCreateEvent
import com.example.apiserver.domain.product.dto.*
import com.example.apiserver.domain.product.repository.ProductRepository
import com.example.apiserver.global.util.CursorPagingHelper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun getProducts(cursorId: Long?, pageSize: Int = 20): CursorPageResponse<ProductResponse, Cursor> {
        val products = productRepository.findWithCursor(cursorId, pageSize + 1)

        val content = products.map(ProductResponse::from)

        val cursorPage = CursorPagingHelper.getCursorPage(
            content = content,
            pageSize = pageSize,
            { Cursor(it.productId) }
        )

        return cursorPage
    }

    @Transactional
    fun createProduct(request: ProductCreateRequest): ProductIdResponse {
        val product = productRepository.save(request.toEntity())

        val event = ProductCreateEvent(
            productId = product.productId,
            name = request.name,
            price = request.price,
            createdAt = product.createdAt
        )
        eventPublisher.publishEvent(event)

        return ProductIdResponse(product.productId)
    }

}