package com.example.apiserver.domain.order.entity

import com.example.apiserver.domain.product.entity.Product
import com.example.core.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderId: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    val product: Product,

    price: Int,
    quantity: Int
) : BaseTimeEntity() {

    init {
        require(price > 0) { "상품 가격은 0보다 커야 합니다." }
        require(quantity > 0) { "주문 수량은 0 이상이어야 합니다." }
    }

    @Column(nullable = false)
    var price: Int = price
        private set

    @Column(nullable = false)
    var quantity: Int = quantity
        private set

}