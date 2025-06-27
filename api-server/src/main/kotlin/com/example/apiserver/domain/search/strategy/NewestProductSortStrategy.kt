package com.example.apiserver.domain.search.strategy

import com.example.apiserver.domain.product.entity.Product
import com.example.apiserver.domain.product.repository.ProductRepository
import com.example.apiserver.domain.search.dto.ProductSearchRequest
import com.example.core.domain.log.entity.ProductSortBy
import org.springframework.stereotype.Component

@Component
class NewestProductSortStrategy(
    private val productRepository: ProductRepository
) : ProductSortStrategy {

    override fun fetchProducts(request: ProductSearchRequest): List<Product> {
        return productRepository.findNewest(
            keyword = request.keyword,
        )
    }

    override fun getSortBy(): ProductSortBy = ProductSortBy.NEWEST

}