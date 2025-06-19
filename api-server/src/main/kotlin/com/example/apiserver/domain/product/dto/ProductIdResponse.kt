package com.example.apiserver.domain.product.dto

import com.example.apiserver.domain.product.entity.Product


data class ProductIdResponse(
    val productId: Long
) {

    companion object {
        fun from(product: Product): ProductIdResponse {
            return ProductIdResponse(productId = product.id)
        }
    }

}