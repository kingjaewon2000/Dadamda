package com.example.core.domain.autocomplete.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "autocomplete_v1")
class AutoCompleteDocument(

    @Id
    @Field(type = FieldType.Keyword)
    val keyword: String,

    @Field(type = FieldType.Text, name = "suggest_ngram")
    val suggestNgram: String,

    @Field(type = FieldType.Long)
    val frequency: Long
)