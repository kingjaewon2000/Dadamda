package com.example.autocompleteserver.domain.autocomplete.repository

import org.springframework.data.domain.Pageable

interface AutoCompleteRepository<T> {

    fun findBySuggestWithPageable(query: String, pageable: Pageable): List<T>

}