package com.example.apiserver.domain.product.service

import com.example.apiserver.domain.product.dto.ProductCreateRequest
import com.example.apiserver.domain.product.dto.ProductUpdateRequest
import com.example.core.domain.product.entity.Product
import com.example.core.domain.product.repository.ProductDocumentRepository
import com.example.core.domain.product.repository.ProductRepository
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
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @InjectMocks
    private lateinit var productService: ProductService

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var productDocumentRepository: ProductDocumentRepository

    @Mock
    private lateinit var eventPublisher: ApplicationEventPublisher

    @Nested
    @DisplayName("상품 등록 테스트")
    inner class CreateProduct {

        @Test
        @DisplayName("상품 등록 성공")
        fun `상품 등록 성공`() {
            // given
            val product = Product(
                productId = 1L,
                name = "상품 테스트",
                price = 100,
                stockQuantity = 10,
            )
            product.createdAt = LocalDateTime.now()

            val request = ProductCreateRequest(
                name = product.name,
                price = product.price,
                stockQuantity = product.stockQuantity
            )
            // when
            whenever(productRepository.save(any<Product>())).thenReturn(product)

            val response = productService.createProduct(request)

            // then
            assertThat(response).isNotNull()
            assertThat(response.productId).isEqualTo(product.productId)
        }

        @Test
        @DisplayName("상품 등록 실패 가격이 0원 이하")
        fun `상품 등록 실패 가격이 0원 이하`() {
            // given
            assertThatThrownBy {
                Product(
                    productId = 1L,
                    name = "가격이 0원인 상품",
                    price = 0,
                    stockQuantity = 10
                )
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품 가격은 0보다 커야 합니다.")
        }

        @Test
        @DisplayName("상품 등록 실패 상품 수량이 -1 이하")
        fun `상품 등록 실패 상품 수량이 -1 이하`() {
            // given
            assertThatThrownBy {
                Product(
                    productId = 1L,
                    name = "가격이 0원인 상품",
                    price = 100,
                    stockQuantity = -1
                )
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품 재고는 0 이상이어야 합니다.")
        }
    }

    @Nested
    @DisplayName("상품 수정 테스트")
    inner class UpdateProduct {
        @Test
        @DisplayName("상품 수정 성공")
        fun `상품 수정 성공`() {
            //given
            val product = Product(
                productId = 1L,
                name = "상품 테스트",
                price = 100,
                stockQuantity = 10,
            )

            val request = ProductUpdateRequest(
                name = "상품 수정 테스트",
                price = 200
            )

            // when
            whenever(productRepository.findById(any())).thenReturn(Optional.of(product))
            val response = productService.updateProduct(product.productId, request)

            // then
            assertThat(response).isNotNull()
            assertThat(response.productId).isEqualTo(product.productId)
        }

        @Test
        @DisplayName("상품 수정 실패 가격이 0원 이하")
        fun `상품 수정 실패 가격이 0원 이하`() {
            //given
            val product = Product(
                productId = 1L,
                name = "상품 테스트",
                price = 100,
                stockQuantity = 10,
            )

            val request = ProductUpdateRequest(
                name = "상품 수정 테스트",
                price = 0
            )

            // when
            whenever(productRepository.findById(any())).thenReturn(Optional.of(product))

            // then
            assertThatThrownBy {
                productService.updateProduct(1L, request)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품 가격은 0보다 커야 합니다.")
        }

    }

}