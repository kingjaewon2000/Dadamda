package com.example.apiserver.domain.dto

data class CursorPageResponse<T, CURSOR>(
    val content: List<T>,
    val hasNext: Boolean,
    val nextCursor: CURSOR?
)