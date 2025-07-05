package com.example.apiserver.domain.product.dto

data class ProductUpdateRequest(
    val name: String,
    val price: Int
)