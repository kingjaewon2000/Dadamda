package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.ProductSort
import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class SearchEngineBestSellingStrategy(
    private val productDocumentRepository: ProductDocumentRepository
) : ProductSearchStrategy<ProductDocument> {

    override fun getSortBy(): ProductSort = ProductSort.BEST_SELLING

    override fun search(request: ProductSearchRequest, pageable: Pageable): List<ProductDocument> {
        val sort = Sort.by(
            Sort.Order.desc("salesCount"),
            Sort.Order.desc("id")
        )

        val pageRequest = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            sort
        )

        return productDocumentRepository.searchByName(request.keyword, pageRequest)
    }

}