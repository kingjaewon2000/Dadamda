package com.example.autocompleteserver.domain.autocomplete.repository

import com.example.autocompleteserver.domain.autocomplete.document.SearchTermDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface ElasticsearchAutoCompleteRepository :
    ElasticsearchRepository<SearchTermDocument, String>,
    AutoCompleteRepository<SearchTermDocument> {

    @Query(
        """
        {
          "multi_match": {
            "query": "?0",
            "type": "bool_prefix",
            "fields": [
              "suggest",
              "suggest._2gram",
              "suggest._3gram"
            ]
          }
        }
    """
    )
    override fun findBySuggestWithPageable(query: String, pageable: Pageable): List<SearchTermDocument>

}
