package com.example.apiserver.domain.event.dto

import com.example.core.domain.product.document.ProductDocument
import java.time.LocalDateTime

data class ProductCreateEvent(
    val productId: Long,
    val name: String,
    val price: Int,
    val stockQuantity: Int,
    val createdAt: LocalDateTime
) {

    fun toDocument(): ProductDocument {
        checkNotNull(productId) { "엘라스틱서치 인덱싱을 위해서는 상품 ID가 null일 수 없습니다." }

        return ProductDocument(
            id = productId,
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            salesCount = 0,
            createdAt = createdAt
        )
    }
    
}