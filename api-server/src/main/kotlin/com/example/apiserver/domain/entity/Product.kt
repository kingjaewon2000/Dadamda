package com.example.apiserver.domain.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Table
@Entity
@EntityListeners(AuditingEntityListener::class)
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false)
    var name: String,

    @Column
    val description: String,

    price: Int,
    stockQuantity: Int
) {

    init {
        require(price > 0) { "상품 가격은 0보다 커야 합니다." }
        require(stockQuantity >= 0) { "상품 재고는 0 이상이어야 합니다." }
    }

    @Column(nullable = false)
    var price: Int = price
        private set

    @Column(nullable = false)
    var stockQuantity: Int = stockQuantity
        private set

    @Column(nullable = false, updatable = false)
    @CreatedDate
    lateinit var createdAt: LocalDateTime
        private set

    @Column(nullable = false)
    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

}