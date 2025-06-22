package com.example.apiserver.domain.order.service

import com.example.apiserver.domain.order.dto.OrderCreateRequest
import com.example.apiserver.domain.order.entity.Order
import com.example.apiserver.domain.order.repository.OrderRepository
import com.example.apiserver.domain.product.entity.Product
import com.example.apiserver.domain.product.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @InjectMocks
    private lateinit var orderService: OrderService

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var productRepository: ProductRepository

    @Nested
    @DisplayName("상품 주문 테스트")
    inner class CreateOrder {

        @Test
        @DisplayName("상품 주문 성공")
        fun `상품 주문 성공`() {
            // given
            val product = Product(
                productId = 1L,
                name = "테스트 상품",
                price = 10000,
                stockQuantity = 100
            )

            val request = OrderCreateRequest(
                productId = product.productId,
                quantity = 10
            )

            val order = Order(
                orderId = 1L,
                product = product,
                price = product.price,
                quantity = request.quantity
            )

            // when
            whenever(productRepository.findByIdForUpdate(any())).thenReturn(product)
            whenever(orderRepository.save(any<Order>())).thenReturn(order)

            val response = orderService.createOrder(request)

            // then
            assertThat(response).isNotNull()
            assertThat(response.orderId).isEqualTo(order.orderId)
        }

        @Test
        @DisplayName("상품 주문 실패 주문 수량이 0 이하")
        fun `주문 수량이 0이하`() {
            // given
            val product = Product(
                productId = 1L,
                name = "테스트 상품",
                price = 10000,
                stockQuantity = 100
            )

            val request = OrderCreateRequest(
                productId = product.productId,
                quantity = -1
            )

            // when
            whenever(productRepository.findByIdForUpdate(any())).thenReturn(product)

            assertThatThrownBy { orderService.createOrder(request) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품의 수량이 0 이하일 수 없습니다.")
        }

        @Test
        @DisplayName("상품 주문 실패 주문 수량이 0인 경우")
        fun `주문 수량이 0인 경우`() {
            // given
            val product = Product(
                productId = 1L,
                name = "테스트 상품",
                price = 10000,
                stockQuantity = 100
            )

            val request = OrderCreateRequest(
                productId = product.productId,
                quantity = 0
            )

            // when
            whenever(productRepository.findByIdForUpdate(any())).thenReturn(product)

            assertThatThrownBy { orderService.createOrder(request) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품의 수량이 0 이하일 수 없습니다.")
        }

        @Test
        @DisplayName("상품 재고보다 초과해서 주문하는 경우")
        fun `상품 재고보다 초과해서 주문하는 경우`() {
            // given
            val product = Product(
                productId = 1L,
                name = "테스트 상품",
                price = 10000,
                stockQuantity = 100
            )

            val request = OrderCreateRequest(
                productId = product.productId,
                quantity = 1000
            )

            // when
            whenever(productRepository.findByIdForUpdate(any())).thenReturn(product)

            assertThatThrownBy { orderService.createOrder(request) }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품 재고가 부족합니다.")
        }

    }

}