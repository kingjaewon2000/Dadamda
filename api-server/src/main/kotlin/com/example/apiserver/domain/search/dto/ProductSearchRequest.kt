package com.example.apiserver.domain.search.dto

import com.example.apiserver.domain.search.dto.ProductSortBy.NEWEST

data class ProductSearchRequest(
    val keyword: String,

    val sortBy: ProductSortBy = NEWEST,
)
