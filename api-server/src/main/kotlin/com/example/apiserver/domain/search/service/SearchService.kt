package com.example.apiserver.domain.search.service

import com.example.apiserver.domain.product.dto.ProductResponse
import com.example.apiserver.domain.product.strategy.ProductSortStrategyFactory
import com.example.apiserver.domain.search.dto.ProductSearchRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchService(
    private val strategyFactory: ProductSortStrategyFactory
) {

    fun searchProducts(request: ProductSearchRequest): List<ProductResponse> {
        val strategy = strategyFactory.getStrategy(request.sortBy)

        val products = strategy.fetchProducts(request)

        return products.map(ProductResponse::from)
    }

}