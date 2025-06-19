package com.example.apiserver.domain.service

import com.example.apiserver.domain.dto.*
import com.example.apiserver.domain.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository
) {

    companion object {
        const val MAX_PAGE_SIZE = 10
    }

    fun getProducts(cursorId: Long?): CursorPageResponse<ProductResponse, Cursor?> {
        val pageSize = MAX_PAGE_SIZE
        val products = productRepository.findNextPage(cursorId ?: 0L, pageSize + 1)

        val hasNext = products.size > pageSize
        val content = if (hasNext) products.subList(0, pageSize) else products
        val productResponses = content.map(ProductResponse::from)
        val nextCursor = if (hasNext) Cursor(content.last().id) else null

        return CursorPageResponse(
            content = productResponses,
            hasNext = hasNext,
            nextCursor = nextCursor
        )
    }

    @Transactional
    fun createProduct(request: ProductCreateRequest): ProductIdResponse {
        val product = request.toEntity()

        return ProductIdResponse.from(productRepository.save(product))
    }

}