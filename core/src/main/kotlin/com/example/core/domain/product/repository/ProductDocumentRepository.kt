package com.example.core.domain.product.repository

import com.example.core.domain.product.document.ProductDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ProductDocumentRepository : ElasticsearchRepository<ProductDocument, String> {

    @Query(
        """
        {
            "multi_match": {
                "query": "?0",
                "fields": ["name^3", "name.ngram"]
            }
        }
    """
    )
    fun searchByName(keyword: String, pageable: Pageable): List<ProductDocument>

}