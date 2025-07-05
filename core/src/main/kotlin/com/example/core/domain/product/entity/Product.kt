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
        validatePrice(price)
        validateStock(stockQuantity)
    }

    @Column(nullable = false)
    var price: Int = price
        private set

    @Column(nullable = false)
    var stockQuantity: Int = stockQuantity
        private set

    // 상품 정보 수정
    fun updateInfo(name: String, price: Int) {
        validatePrice(price)

        this.name = name
        this.price = price
    }

    // 재고 감소
    fun decreaseStock(quantity: Int) {
        require(quantity > 0) { "감소시킬 재고 수량은 0보다 커야 합니다." }

        if (stockQuantity - quantity < 0) {
            throw IllegalArgumentException("상품 재고가 부족합니다.")
        }

        stockQuantity -= quantity
    }

    // 유효성 검증 로직
    private fun validatePrice(price: Int) {
        require(price > 0) { "상품 가격은 0보다 커야 합니다." }
    }

    private fun validateStock(stockQuantity: Int) {
        require(stockQuantity >= 0) { "상품 재고는 0 이상이어야 합니다." }
    }

}