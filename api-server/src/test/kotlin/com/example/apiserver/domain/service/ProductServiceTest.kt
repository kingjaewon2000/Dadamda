package com.example.apiserver.domain.service

import com.example.apiserver.domain.product.dto.Cursor
import com.example.apiserver.domain.product.dto.CursorPageResponse
import com.example.apiserver.domain.product.dto.ProductCreateRequest
import com.example.apiserver.domain.product.dto.ProductResponse
import com.example.apiserver.domain.product.entity.Product
import com.example.apiserver.domain.product.repository.ProductRepository
import com.example.apiserver.domain.product.service.ProductService
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
class ProductServiceTest {

    @InjectMocks
    private lateinit var productService: ProductService

    @Mock
    private lateinit var productRepository: ProductRepository

    @Nested
    @DisplayName("상품 등록 테스트")
    inner class CreateProduct {

        @Test
        @DisplayName("상품 등록 성공")
        fun `상품 등록 성공`() {
            // given
            val product = Product(
                id = 1L,
                name = "상품 테스트",
                price = 100,
                stockQuantity = 10
            )

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
            assertThat(response.productId).isEqualTo(product.id)
        }

        @Test
        @DisplayName("상품 등록 실패 가격이 0원 이하라면")
        fun `상품 등록 실패1`() {
            // given
            assertThatThrownBy {
                Product(
                    id = 1L,
                    name = "가격이 0원인 상품",
                    price = 0,
                    stockQuantity = 10
                )
            }
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("상품 가격은 0보다 커야 합니다.")
        }

        @Test
        @DisplayName("상품 등록 실패 수량이 -1 이하라면")
        fun `상품 등록 실패2`() {
            // given
            assertThatThrownBy {
                Product(
                    id = 1L,
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
    @DisplayName("상품 목록 조회 테스트")
    inner class GetProduct {

        @Test
        @DisplayName("첫 페이지 요청 다음 페이지 없음")
        fun `첫 페이지 요청 다음 페이지 x`() {
            // given
            val product = Product(
                id = 1L,
                name = "상품 테스트",
                price = 100,
                stockQuantity = 10
            )

            val cursorId = null

            // when
            whenever(productRepository.findNextPage(any(), any())).thenReturn(listOf(product))

            val response: CursorPageResponse<ProductResponse, Cursor?> = productService.getProducts(cursorId)

            // then
            assertThat(response).isNotNull()
            assertThat(response.content).isNotEmpty()
            assertThat(response.content).hasSize(1)
            assertThat(response.hasNext).isFalse()
            assertThat(response.nextCursor).isNull()
        }

        @Test
        @DisplayName("첫 페이지 요청 다음 페이지 있음")
        fun `첫 페이지 요청 다음 페이지 o`() {
            // given
            val pageSize = 10
            val products = (1L..pageSize + 1).map {
                Product(
                    id = it,
                    name = "상품 테스트 ${it + 1}",
                    price = 100,
                    stockQuantity = 10
                )
            }

            val cursorId = null

            // when
            whenever(productRepository.findNextPage(any(), any<Int>())).thenReturn(products)

            val response: CursorPageResponse<ProductResponse, Cursor?> = productService.getProducts(cursorId)

            // then
            assertThat(response).isNotNull()
            assertThat(response.content).isNotEmpty()
            assertThat(response.content).hasSize(pageSize)
            assertThat(response.hasNext).isTrue()
            assertThat(response.nextCursor).isNotNull()
        }

    }

}