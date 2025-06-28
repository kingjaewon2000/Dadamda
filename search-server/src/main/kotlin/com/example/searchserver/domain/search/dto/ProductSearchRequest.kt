package com.example.searchserver.domain.search.dto

import com.example.core.domain.log.entity.ProductSortBy


data class ProductSearchRequest(
    val keyword: String,

    val sortBy: ProductSortBy = ProductSortBy.NEWEST,
)