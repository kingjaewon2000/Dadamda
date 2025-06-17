package com.example.apiserver.domain.dto

import com.example.apiserver.domain.entity.Product

data class ProductCreateRequest(
    val name: String,
    val description: String,
    val price: Int,
    val stockQuantity: Int
) {

    fun toEntity(): Product {
        return Product(
            name = name,
            description = description,
            price = price,
            stockQuantity = stockQuantity
        )
    }

}