package com.example.apiserver.domain.controller

import com.example.apiserver.domain.dto.ProductCreateRequest
import com.example.apiserver.domain.dto.ProductIdResponse
import com.example.apiserver.domain.service.ProductService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun createProduct(@RequestBody request: ProductCreateRequest): ProductIdResponse {
        return productService.createProduct(request)
    }

}