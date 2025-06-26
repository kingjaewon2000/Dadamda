package com.example.autocompleteserver.domain.autocomplete.service

import com.example.autocompleteserver.domain.autocomplete.dto.AutoCompleteResponse

interface AutoCompleteService {

    fun getSuggestions(query: String): List<AutoCompleteResponse>

}