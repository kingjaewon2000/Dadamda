package com.example.autocompleteserver.domain.autocomplete.controller

import com.example.autocompleteserver.domain.autocomplete.dto.AutoCompleteResponse
import com.example.autocompleteserver.domain.autocomplete.service.AutoCompleteService
import com.example.core.global.dto.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auto-complete")
class AutoCompleteController(
    private val autocompleteService: AutoCompleteService
) {

    @GetMapping
    fun getSuggestions(@RequestParam query: String): ApiResponse<List<AutoCompleteResponse>> {
        if (query.isBlank()) {
            return ApiResponse.success(emptyList())
        }

        return ApiResponse.success(autocompleteService.getSuggestions(query.lowercase()))
    }

}