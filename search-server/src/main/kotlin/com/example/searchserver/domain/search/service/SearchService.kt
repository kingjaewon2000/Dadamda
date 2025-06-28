package com.example.searchserver.domain.search.service

import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.dto.ProductResponse
import com.example.searchserver.domain.log.service.LogService
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import com.example.searchserver.domain.search.strategy.ProductSearchStrategyFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SearchService(
    private val logService: LogService,
    private val strategyFactory: ProductSearchStrategyFactory<ProductDocument>,
) {

    fun searchProducts(request: ProductSearchRequest): List<ProductResponse> {
        val strategy = strategyFactory.getStrategy(request.sortBy)

        val products = strategy.search(request)

        logService.log(request.sortBy, request.keyword)

        return products.map(ProductResponse::from)
    }

}