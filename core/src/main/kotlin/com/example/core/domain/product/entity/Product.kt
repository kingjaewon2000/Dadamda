package com.example.core.domain.product.entity

import com.example.core.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var productId: Long = 0L,

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

    fun decreaseStock(quantity: Int) {
        if (quantity <= 0) {
            throw IllegalArgumentException("상품의 수량이 0 이하일 수 없습니다.")
        }

        if (stockQuantity - quantity < 0) {
            throw IllegalArgumentException("상품 재고가 부족합니다.")
        }

        stockQuantity -= quantity
    }

}