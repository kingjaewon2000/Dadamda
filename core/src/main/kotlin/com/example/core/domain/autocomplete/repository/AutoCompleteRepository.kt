package com.example.core.domain.autocomplete.repository

import com.example.core.domain.autocomplete.document.AutoCompleteDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface AutoCompleteRepository : ElasticsearchRepository<AutoCompleteDocument, String>, CustomAutoCompleteRepository {

    @Query(
        """
        {
          "simple_query_string": {
            "query": "?0",
            "fields": [ "suggest_ngram" ],
            "default_operator": "AND"
          }
        }
    """
    )
    fun findBySuggestWithPageable(query: String, pageable: Pageable): List<AutoCompleteDocument>

}