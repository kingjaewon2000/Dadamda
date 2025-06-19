package com.example.apiserver.domain.entity

import com.example.core.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Table
@Entity
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false)
    var name: String,

    price: Int,
    stockQuantity: Int
) : BaseTimeEntity() {

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

}