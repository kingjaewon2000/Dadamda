package com.example.apiserver.domain.product.dto

import com.example.core.domain.product.entity.Product


data class ProductCreateRequest(
    val name: String,
    val price: Int,
    val stockQuantity: Int
) {

    fun toEntity(): Product {
        return Product(
            name = name,
            price = price,
            stockQuantity = stockQuantity
        )
    }

}