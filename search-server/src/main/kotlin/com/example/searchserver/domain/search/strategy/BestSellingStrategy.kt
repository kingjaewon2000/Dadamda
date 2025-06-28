package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.core.domain.product.entity.Product
import com.example.core.domain.product.repository.ProductRepository
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.stereotype.Component

@Component
class BestSellingStrategy(
    private val productRepository: ProductRepository
) : ProductSearchStrategy<Product> {

    override fun getSortBy(): ProductSort = ProductSort.BEST_SELLING

    override fun search(request: ProductSearchRequest): List<Product> {
        return productRepository.findBestSelling(
            keyword = request.keyword,
        )
    }

}