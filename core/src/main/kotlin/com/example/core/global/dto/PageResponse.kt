package com.example.core.global.dto

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val code: Int,
    val message: String,
    val results: List<T>,

    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Long,
    val isLast: Boolean
) {

    companion object {
        private const val SUCCESS_CODE = 200
        private const val SUCCESS_MESSAGE = "success"

        fun <T> fromPage(page: Page<T>): PageResponse<T> {
            return PageResponse(
                code = SUCCESS_CODE,
                message = SUCCESS_MESSAGE,
                results = page.content,
                pageNumber = page.number,
                pageSize = page.size,
                totalPages = page.totalPages,
                totalElements = page.totalElements,
                isLast = page.isLast
            )
        }
    }

}