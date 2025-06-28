package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import org.springframework.stereotype.Component

@Component
class ProductSearchStrategyFactory<T>(
    private val strategies: List<ProductSearchStrategy<T>>
) {

    private val strategyMap: Map<ProductSort, ProductSearchStrategy<T>> = strategies.associateBy { it.getSortBy() }

    fun getStrategy(productSort: ProductSort): ProductSearchStrategy<T> {
        return strategyMap[productSort] ?: throw IllegalArgumentException("지원하지 않는 정렬 옵션입니다.")
    }

}