package com.example.apiserver.domain.product.controller

import com.example.apiserver.domain.product.dto.*
import com.example.apiserver.domain.product.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getProducts(
        @RequestParam cursorId: Long?
    ): CursorPageResponse<ProductResponse, Cursor?> {
        return productService.getProducts(cursorId)
    }

    @PostMapping
    fun createProduct(@RequestBody request: ProductCreateRequest): ProductIdResponse {
        return productService.createProduct(request)
    }

}