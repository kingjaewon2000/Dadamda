package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSortBy
import com.example.core.domain.product.entity.Product
import com.example.core.domain.product.repository.ProductRepository
import com.example.searchserver.domain.search.dto.ProductSearchRequest
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