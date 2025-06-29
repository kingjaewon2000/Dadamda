package com.example.core.domain.product.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import java.time.LocalDateTime

@Document(indexName = "products")
@Setting(settingPath = "elasticsearch/settings/product.json")
class ProductDocument(
    @Id
    @Field(type = FieldType.Long)
    val id: Long,

    @MultiField(
        mainField = Field(type = FieldType.Text, analyzer = "korean_analyzer"),
        otherFields = [
            InnerField(
                suffix = "ngram",
                type = FieldType.Text,
                analyzer = "ngram_analyzer"
            )
        ]
    )
    val name: String,

    @Field(type = FieldType.Integer)
    val price: Int,

    // 판매량 순 정렬을 위한 필드
    @Field(type = FieldType.Long)
    val salesCount: Long = 0,

    // 신규 상품 순 정렬을 위한 필드
    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis])
    val createdAt: LocalDateTime
)