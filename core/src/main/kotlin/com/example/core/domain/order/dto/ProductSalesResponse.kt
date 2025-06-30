package com.example.core.domain.order.dto

import java.time.LocalDateTime

data class ProductSalesResponse(
    val productId: Long,
    val name: String,
    val price: Int,
    val stockQuantity: Int,
    val salesCount: Long,
    val createdAt: LocalDateTime
)
