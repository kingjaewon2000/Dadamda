package com.example.apiserver.domain.product.strategy

import com.example.core.domain.log.entity.ProductSortBy
import org.springframework.stereotype.Component

@Component
class ProductSortStrategyFactory(
    private val strategies: List<ProductSortStrategy>
) {

    private val strategyMap: Map<ProductSortBy, ProductSortStrategy> = strategies.associateBy { it.getSortBy() }

    fun getStrategy(sortBy: ProductSortBy): ProductSortStrategy {
        return strategyMap[sortBy] ?: throw IllegalArgumentException("지원하지 않는 정렬 옵션입니다.")
    }

}