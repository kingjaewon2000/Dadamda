package com.example.searchserver.domain.search.controller

import com.example.core.domain.product.dto.ProductResponse
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import com.example.searchserver.domain.search.service.SearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class SearchController(
    private val searchService: SearchService
) {

    @GetMapping
    fun searchProducts(
        @ModelAttribute request: ProductSearchRequest
    ): List<ProductResponse> {
        return searchService.searchProducts(request)
    }

}