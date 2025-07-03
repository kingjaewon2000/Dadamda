package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.data.domain.Pageable

interface ProductSearchStrategy<T> {

    val hasNextPage: Int
        get() = 1

    fun search(request: ProductSearchRequest, pageable: Pageable): List<T>

    fun getSortBy(): ProductSort

}