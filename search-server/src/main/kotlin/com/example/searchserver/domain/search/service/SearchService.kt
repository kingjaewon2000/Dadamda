package com.example.searchserver.domain.search.service

import com.example.core.domain.product.dto.ProductResponse
import com.example.searchserver.domain.log.service.LogService
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import com.example.searchserver.domain.search.strategy.ProductSortStrategyFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchService(
    private val strategyFactory: ProductSortStrategyFactory,
//    private val logService: LogService,
) {

    fun searchProducts(request: ProductSearchRequest): List<ProductResponse> {
        val strategy = strategyFactory.getStrategy(request.sortBy)

        val products = strategy.fetchProducts(request)

//        logService.log(request.sortBy, request.keyword)

        return products.map(ProductResponse::from)
    }

}