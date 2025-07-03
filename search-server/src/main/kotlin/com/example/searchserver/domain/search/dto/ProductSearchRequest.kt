package com.example.searchserver.domain.search.dto

import com.example.core.domain.log.entity.SortOption


data class ProductSearchRequest(
    val keyword: String,

    val sortOption: SortOption = SortOption.NEWEST,
)