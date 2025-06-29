package com.example.core.domain.autocomplete.repository

import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.document.Document
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.ScriptType
import org.springframework.data.elasticsearch.core.query.UpdateQuery
import org.springframework.stereotype.Repository

@Repository
class AutoCompleteRepositoryImpl(
    private val elasticsearchOperations: ElasticsearchOperations
) : CustomAutoCompleteRepository {

    override fun bulkUpdateFrequency(frequencyMap: Map<String, Long>) {
        if (frequencyMap.isEmpty()) {
            return
        }

        val updateQueries = frequencyMap.map { (keyword, count) ->
            val script = "ctx._source.frequency += params.count"
            val params = mapOf("count" to count)

            UpdateQuery.builder(keyword)
                .withScript(script)
                .withScriptType(ScriptType.INLINE)
                .withParams(params)
                .withScriptedUpsert(true)
                .withUpsert(
                    Document.from(
                        mapOf(
                            "keyword" to keyword,
                            "suggest_ngram" to keyword,
                            "frequency" to count
                        )
                    )
                )
                .build()
        }

        elasticsearchOperations.bulkUpdate(updateQueries, IndexCoordinates.of("autocomplete"))
    }

}