package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.searchserver.domain.search.dto.ProductSearchRequest

interface ProductSearchStrategy<T> {

    val hasNextPage: Int
        get() = 1

    fun search(request: ProductSearchRequest): List<T>

    fun getSortBy(): ProductSort

}