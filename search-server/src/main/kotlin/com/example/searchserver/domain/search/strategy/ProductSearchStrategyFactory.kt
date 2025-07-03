package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.SortOption
import org.springframework.stereotype.Component

@Component
class ProductSearchStrategyFactory<T>(
    private val strategies: List<ProductSearchStrategy<T>>
) {

    private val strategyMap: Map<SortOption, ProductSearchStrategy<T>> = strategies.associateBy { it.getSortOption() }

    fun getStrategy(sortOption: SortOption): ProductSearchStrategy<T> {
        return strategyMap[sortOption] ?: throw IllegalArgumentException("지원하지 않는 정렬 옵션입니다.")
    }

}