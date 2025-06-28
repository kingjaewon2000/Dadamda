package com.example.core.domain.product.dto

import com.example.core.domain.product.entity.Product

data class ProductResponse(
    val productId: Long,
    val name: String,
    val price: Int,
) {

    companion object {
        fun from(product: Product): ProductResponse {
            return ProductResponse(
                productId = product.productId,
                name = product.name,
                price = product.price,
            )
        }
    }

}
