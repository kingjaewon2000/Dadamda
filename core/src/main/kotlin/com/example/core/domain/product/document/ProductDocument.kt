package com.example.core.domain.product.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.*
import java.time.LocalDateTime

@Document(indexName = "product_alias", createIndex = false)
@Setting(settingPath = "elasticsearch/settings/product.json")
class ProductDocument(
    @Id
    @Field(type = FieldType.Long)
    val id: Long,

    name: String,
    price: Int,

    @Field(type = FieldType.Integer)
    val stockQuantity: Int,

    // 판매량 순 정렬을 위한 필드
    @Field(type = FieldType.Long)
    val salesCount: Long = 0,

    // 신규 상품 순 정렬을 위한 필드
    @Field(type = FieldType.Date, format = [DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis])
    val createdAt: LocalDateTime
) {

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
    var name: String = name
        private set

    @Field(type = FieldType.Integer)
    var price: Int = price
        private set

    fun updateInfo(name: String, price: Int) {
        require(price > 0) { "상품 가격은 0보다 커야 합니다." }

        this.name = name
        this.price = price
    }

}