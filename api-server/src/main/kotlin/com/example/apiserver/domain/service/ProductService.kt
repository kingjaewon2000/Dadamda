package com.example.apiserver.domain.service

import com.example.apiserver.domain.dto.ProductCreateRequest
import com.example.apiserver.domain.dto.ProductIdResponse
import com.example.apiserver.domain.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productRepository: ProductRepository
) {

    @Transactional
    fun createProduct(request: ProductCreateRequest): ProductIdResponse {
        val product = request.toEntity()

        return ProductIdResponse.from(productRepository.save(product))
    }

}