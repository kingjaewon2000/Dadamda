package com.example.core.domain.log.entity

enum class SortOption(val description: String) {

    NEWEST("신상품순"),
    BEST_SELLING("판매량순");

    companion object {
        fun from(value: String?): SortOption {
            return entries.find { it.name.lowercase() == value } ?: BEST_SELLING
        }
    }

}