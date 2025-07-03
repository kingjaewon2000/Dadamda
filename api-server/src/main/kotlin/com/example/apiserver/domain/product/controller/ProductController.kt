package com.example.apiserver.domain.product.controller

import com.example.apiserver.domain.product.service.ProductService
import com.example.core.domain.product.dto.ProductCreateRequest
import com.example.core.domain.product.dto.ProductIdResponse
import com.example.core.domain.product.dto.ProductResponse
import com.example.core.global.dto.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/top10")
    fun getTop10Products(): ApiResponse<List<ProductResponse>> {
        return ApiResponse.success(productService.getTop10Products())
    }

    @PostMapping
    fun createProduct(@RequestBody request: ProductCreateRequest): ApiResponse<ProductIdResponse> {
        return ApiResponse.create(productService.createProduct(request))
    }

}