package com.example.autocompleteserver.domain.autocomplete.service

import com.example.autocompleteserver.domain.autocomplete.dto.AutoCompleteResponse
import com.example.core.domain.autocomplete.repository.AutoCompleteRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ElasticsearchAutoCompleteService(
    private val autocompleteRepository: AutoCompleteRepository,
) : AutoCompleteService {

    companion object {
        private const val PAGE_NUMBER = 0
        private const val PAGE_SIZE = 10
    }

    override fun getSuggestions(query: String): List<AutoCompleteResponse> {
        val sortOption = Sort.by(Sort.Direction.DESC, "frequency")
        val pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, sortOption)
        val searchResults = autocompleteRepository.findBySuggestWithPageable(query, pageable)

        return searchResults.map { AutoCompleteResponse(it.keyword) }
    }

}