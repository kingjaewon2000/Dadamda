package com.example.apiserver.domain.order.controller

import com.example.apiserver.domain.order.dto.OrderCreateRequest
import com.example.apiserver.domain.order.service.OrderService
import com.example.apiserver.domain.product.entity.Product
import com.example.apiserver.domain.product.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class OrderControllerTest {

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Nested
    @DisplayName("주문 동시성 테스트")
    inner class ConcurrencyTest {

        @Test
        @DisplayName("100개의 요청이 동시에 하나의 상품 재고를 1씩 감소시키는 경우")
        fun `100개의 요청이 동시에 하나의 상품 재고를 1씩 감소시키는 경우`() {
            // given
            val initialStock = 100
            val numberOfThreads = 100
            val executorService = Executors.newFixedThreadPool(32)
            val product = productRepository.save(Product(name = "테스트 상품", price = 1000, stockQuantity = initialStock))

            // when
            val latch = CountDownLatch(numberOfThreads)

            for (i in 1..numberOfThreads) {
                executorService.submit {
                    try {
                        orderService.createOrder(OrderCreateRequest(product.productId, 1))
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            val findProduct = productRepository.findById(product.productId).get()

            // then
            assertThat(findProduct.stockQuantity).isEqualTo(0)
        }

        @Test
        @DisplayName("재고가 1개일 때 100개의 동시 주문이 발생하면, 1명만 성공하고 99명은 실패해야 한다")
        fun `임계점 동시성 테스트`() {
            // given
            val initialStock = 1
            val numberOfThreads = 100
            val exceptionCount = AtomicInteger(0)
            val executorService = Executors.newFixedThreadPool(32)
            val product = productRepository.save(Product(name = "테스트 상품", price = 1000, stockQuantity = initialStock))

            // when
            val latch = CountDownLatch(numberOfThreads)

            for (i in 1..numberOfThreads) {
                executorService.submit {
                    try {
                        orderService.createOrder(OrderCreateRequest(product.productId, 1))
                    } catch (e: IllegalArgumentException) {
                        exceptionCount.incrementAndGet()
                    } finally {
                        latch.countDown()
                    }
                }
            }
            latch.await()

            val findProduct = productRepository.findById(product.productId).get()

            // then
            assertThat(findProduct.stockQuantity).isEqualTo(0)
            assertThat(exceptionCount.get()).isEqualTo(99)
        }

    }

}