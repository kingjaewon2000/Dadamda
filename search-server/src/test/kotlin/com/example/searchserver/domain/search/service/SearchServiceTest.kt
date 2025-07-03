package com.example.searchserver.domain.search.service

import com.example.core.domain.log.entity.SortOption
import com.example.core.domain.product.document.ProductDocument
import com.example.searchserver.domain.log.service.LogService
import com.example.searchserver.domain.search.dto.ProductSearchRequest
import com.example.searchserver.domain.search.strategy.ProductSearchStrategyFactory
import com.example.searchserver.domain.search.strategy.SearchEngineBestSellingStrategy
import com.example.searchserver.domain.search.strategy.SearchEngineNewestProductStrategy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class SearchServiceTest {

    @InjectMocks
    private lateinit var searchService: SearchService

    @Mock
    private lateinit var logService: LogService

    @Mock
    private lateinit var strategyFactory: ProductSearchStrategyFactory<ProductDocument>

    @Mock
    private lateinit var bestSellingStrategy: SearchEngineBestSellingStrategy

    @Mock
    private lateinit var newestStrategy: SearchEngineNewestProductStrategy


    @Nested
    @DisplayName("상품 검색 테스트")
    inner class GetProductSearch {

        private lateinit var newestRequest: ProductSearchRequest
        private lateinit var bestSellingRequest: ProductSearchRequest
        private lateinit var pageable: Pageable
        private lateinit var products: Page<ProductDocument>

        @BeforeEach
        fun setUp() {
            newestRequest = ProductSearchRequest("노트북", SortOption.NEWEST)
            bestSellingRequest = ProductSearchRequest("노트북", SortOption.BEST_SELLING)
            pageable = PageRequest.of(0, 10)

            val productList = listOf(
                ProductDocument(
                    id = 1,
                    name = "맥북",
                    price = 1500000,
                    stockQuantity = 10,
                    salesCount = 0,
                    createdAt = LocalDateTime.now()
                )
            )
            products = PageImpl(productList, pageable, 1)
        }

        @Test
        @DisplayName("상품 검색 시 판매량순 검색 성공")
        fun `판매량순 검색 성공`() {
            whenever(strategyFactory.getStrategy(SortOption.BEST_SELLING)).thenReturn(bestSellingStrategy)
            whenever(bestSellingStrategy.search(bestSellingRequest, pageable)).thenReturn(products)

            val responses = searchService.searchProducts(bestSellingRequest, pageable)

            // then
            assertThat(responses.content).hasSize(1)
            assertThat(responses.content.first().name).isEqualTo("맥북")
            assertThat(responses.totalPages).isEqualTo(1)
        }

        @Test
        @DisplayName("판매량순 검색 시 검색 결과가 없을 때")
        fun `판매량순 검색 시 검색 결과가 없을 때`() {
            // given
            val emptyResults = Page.empty<ProductDocument>(pageable)

            // when
            whenever(strategyFactory.getStrategy(SortOption.BEST_SELLING)).thenReturn(bestSellingStrategy)
            whenever(bestSellingStrategy.search(bestSellingRequest, pageable)).thenReturn(emptyResults)

            val responses = searchService.searchProducts(bestSellingRequest, pageable)

            // then
            assertThat(responses.isEmpty).isTrue()
            assertThat(responses.content).isEmpty()
        }

        @Test
        @DisplayName("신규상품순 검색 성공")
        fun `신규상품순 검색 성공`() {
            // when
            whenever(strategyFactory.getStrategy(SortOption.NEWEST)).thenReturn(newestStrategy)
            whenever(newestStrategy.search(newestRequest, pageable)).thenReturn(products)

            val responses = searchService.searchProducts(newestRequest, pageable)

            // then
            assertThat(responses.content).hasSize(1)
            assertThat(responses.content.first().name).isEqualTo("맥북")
            assertThat(responses.totalPages).isEqualTo(1)
        }

        @Test
        @DisplayName("신규상품순 검색 시 검색 결과가 없을 때")
        fun `신규상품순 검색 시 검색 결과가 없을 때`() {
            // given
            val emptyResults = Page.empty<ProductDocument>(pageable)

            // when
            whenever(strategyFactory.getStrategy(SortOption.NEWEST)).thenReturn(newestStrategy)
            whenever(newestStrategy.search(newestRequest, pageable)).thenReturn(emptyResults)

            val responses = searchService.searchProducts(newestRequest, pageable)

            // then
            assertThat(responses.isEmpty).isTrue()
            assertThat(responses.content).isEmpty()
        }
    }

}

