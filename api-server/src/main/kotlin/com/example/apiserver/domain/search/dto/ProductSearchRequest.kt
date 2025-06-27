package com.example.apiserver.domain.search.dto

import com.example.core.domain.log.entity.ProductSortBy
import com.example.core.domain.log.entity.ProductSortBy.NEWEST

data class ProductSearchRequest(
    val keyword: String,

    val sortBy: ProductSortBy = NEWEST,
)
