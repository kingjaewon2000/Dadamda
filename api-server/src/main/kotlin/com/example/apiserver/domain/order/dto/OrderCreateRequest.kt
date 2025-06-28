package com.example.apiserver.domain.order.dto

import com.example.core.domain.order.entity.Order
import com.example.core.domain.product.entity.Product

data class OrderCreateRequest(
    val productId: Long,
    val quantity: Int
) {

    fun toEntity(product: Product): Order {
        return Order(
            product = product,
            price = product.price,
            quantity = quantity
        )
    }

}