package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class SearchEngineNewestProductStrategy(
    private val productDocumentRepository: ProductDocumentRepository
) : ProductSearchStrategy<ProductDocument> {

    override fun getSortBy(): ProductSort = ProductSort.NEWEST

    override fun search(request: ProductSearchRequest): List<ProductDocument> {
        val sort = Sort.by(
            Sort.Order.desc("createdAt"),
            Sort.Order.desc("id")
        )

        return productDocumentRepository.searchByName(request.keyword, sort)
    }

}