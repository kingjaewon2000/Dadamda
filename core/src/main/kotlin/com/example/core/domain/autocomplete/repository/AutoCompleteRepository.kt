package com.example.core.domain.autocomplete.repository

import com.example.core.domain.autocomplete.document.AutoCompleteDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface AutoCompleteRepository : ElasticsearchRepository<AutoCompleteDocument, String>, CustomAutoCompleteRepository {

    @Query("""
        {
          "match": {
            "suggest_ngram": {
              "query": "?0"
            }
          }
        }
    """)
    fun findBySuggestWithPageable(query: String, pageable: Pageable): List<AutoCompleteDocument>

}