package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.core.domain.product.entity.Product
import com.example.core.domain.product.repository.ProductRepository
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class NewestProductStrategy(
    private val productRepository: ProductRepository
) : ProductSearchStrategy<Product> {

    override fun getSortBy(): ProductSort = ProductSort.NEWEST

    override fun search(request: ProductSearchRequest, pageable: Pageable): Page<Product> {
        return productRepository.findNewest(
            keyword = request.keyword,
            pageable = pageable,
        )
    }

}