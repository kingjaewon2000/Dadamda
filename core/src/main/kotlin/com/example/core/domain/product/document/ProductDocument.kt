package com.example.core.domain.product.document

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDateTime

@Document(indexName = "products")
class ProductDocument(
    @Id
    val id: String,

    @Field(type = FieldType.Text, name = "name")
    val name: String,

    @Field(type = FieldType.Integer, name = "price")
    val price: Int,

    // 판매량 순 정렬을 위한 필드
    @Field(type = FieldType.Long, name = "salesCount")
    val salesCount: Long = 0,

    // 신규 상품 순 정렬을 위한 필드
    @Field(type = FieldType.Date, name = "createdAt")
    val createdAt: LocalDateTime
)