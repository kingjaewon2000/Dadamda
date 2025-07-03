package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductSearchStrategy<T> {
    
    fun search(request: ProductSearchRequest, pageable: Pageable): Page<T>

    fun getSortBy(): ProductSort

}