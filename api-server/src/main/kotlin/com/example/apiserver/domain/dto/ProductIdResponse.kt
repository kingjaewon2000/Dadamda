package com.example.apiserver.domain.dto

import com.example.apiserver.domain.entity.Product

data class ProductIdResponse(
    val productId: Long
) {

    companion object {
        fun from(product: Product): ProductIdResponse {
            return ProductIdResponse(productId = product.id)
        }
    }

}