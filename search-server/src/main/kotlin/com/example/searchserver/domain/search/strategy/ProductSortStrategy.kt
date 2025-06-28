package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSortBy
import com.example.core.domain.product.entity.Product
import com.example.searchserver.domain.search.dto.ProductSearchRequest

interface ProductSortStrategy {

    val hasNextPage: Int
        get() = 1

    fun fetchProducts(request: ProductSearchRequest): List<Product>

    fun getSortBy(): ProductSortBy

}