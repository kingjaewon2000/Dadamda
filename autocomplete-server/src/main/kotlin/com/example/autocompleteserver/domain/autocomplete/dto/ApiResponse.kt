package com.example.autocompleteserver.domain.autocomplete.dto

data class ApiResponse<T>(
    val results: List<T>
)