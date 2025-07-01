package com.example.core.domain.autocomplete.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting

@Document(indexName = "autocomplete")
@Setting(settingPath = "elasticsearch/settings/autocomplete.json")
class AutoCompleteDocument(

    @Id
    @Field(type = FieldType.Keyword)
    val keyword: String,

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", name = "suggest_ngram")
    val suggestNgram: String,

    @Field(type = FieldType.Long)
    val frequency: Long
)