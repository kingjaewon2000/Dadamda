package com.example.autocompleteserver.domain.autocomplete.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "autocomplete_v1")
class SearchTermDocument(
    @Id
    val id: String,

    @Field(type = FieldType.Search_As_You_Type)
    val suggest: String,

    @Field(type = FieldType.Long)
    val frequency: Long,

    @Field(type = FieldType.Keyword)
    val keyword: String
)