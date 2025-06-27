package com.example.core.domain.autocomplete.repository

interface CustomAutoCompleteRepository {

    fun bulkUpdateFrequency(frequencyMap: Map<String, Long>)
}