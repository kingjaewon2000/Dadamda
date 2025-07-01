package com.example.apiserver.domain.product.controller

import com.example.apiserver.domain.product.service.ProductService
import com.example.core.domain.product.dto.Cursor
import com.example.core.domain.product.dto.CursorPageResponse
import com.example.core.domain.product.dto.ProductCreateRequest
import com.example.core.domain.product.dto.ProductIdResponse
import com.example.core.domain.product.dto.ProductResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getProducts(
        @RequestParam cursorId: Long?,
        @RequestParam pageSize: Int = 20
    ): CursorPageResponse<ProductResponse, Cursor> {
        return productService.getProducts(cursorId, pageSize)
    }

    @GetMapping("/top10")
    fun getTop10Products(): List<ProductResponse> {
        return productService.getTop10Products()
    }

    @PostMapping
    fun createProduct(@RequestBody request: ProductCreateRequest): ProductIdResponse {
        return productService.createProduct(request)
    }

}