package com.example.searchserver.domain.search.strategy

import com.example.core.domain.log.entity.SortOption
import com.example.core.domain.product.document.ProductDocument
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class SearchEngineBestSellingStrategy(
    private val productDocumentRepository: ProductDocumentRepository
) : ProductSearchStrategy<ProductDocument> {

    override fun getSortOption(): SortOption = SortOption.BEST_SELLING

    override fun search(request: ProductSearchRequest, pageable: Pageable): Page<ProductDocument> {
        val sortOption = Sort.by(
            Sort.Order.desc("salesCount"),
            Sort.Order.desc("id")
        )

        val pageRequest = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            sortOption
        )

        return productDocumentRepository.searchByName(request.keyword, pageRequest)
    }

}