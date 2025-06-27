package com.example.core.domain.log.entity

enum class ProductSortBy(val description: String) {

    NEWEST("신상품순"),
    BEST_SELLING("판매량순");

    companion object {
        fun from(value: String?): ProductSortBy {
            return entries.find { it.name.lowercase() == value } ?: BEST_SELLING
        }
    }

}