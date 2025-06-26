package com.example.apiserver.domain.product.strategy

import com.example.apiserver.domain.product.entity.Product
import com.example.apiserver.domain.search.dto.ProductSearchRequest
import com.example.core.domain.log.entity.ProductSortBy

interface ProductSortStrategy {

    val hasNextPage: Int
        get() = 1

    fun fetchProducts(request: ProductSearchRequest): List<Product>

    fun getSortBy(): ProductSortBy

}