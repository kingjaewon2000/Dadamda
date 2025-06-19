package com.example.apiserver.domain.dto

import com.example.apiserver.domain.entity.Product

data class ProductResponse(
    val productId: Long,
    val name: String,
    val price: Int,
) {

    companion object {
        fun from(product: Product): ProductResponse {
            return ProductResponse(
                productId = product.id,
                name = product.name,
                price = product.price,
            )
        }
    }

}
