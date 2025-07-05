package com.example.apiserver.domain.event.dto

data class ProductUpdateEvent(
    val productId: Long,
    val name: String,
    val price: Int
)