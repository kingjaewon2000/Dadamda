package com.example.autocompleteserver.domain.autocomplete.service

import com.example.autocompleteserver.domain.autocomplete.dto.AutoCompleteResponse
import com.example.core.domain.autocomplete.document.AutoCompleteDocument
import com.example.core.domain.autocomplete.repository.AutoCompleteRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ElasticsearchAutoCompleteServiceTest {

    @InjectMocks
    private lateinit var autoCompleteService: ElasticsearchAutoCompleteService

    @Mock
    private lateinit var autoCompleteRepository: AutoCompleteRepository

    @Nested
    @DisplayName("검색어 자동완성 테스트")
    inner class GetAutoComplete {

        @Test
        @DisplayName("자동완성 목록 조회 성공")
        fun `검색어 자동완성 목록 조회 성공`() {
            // given
            val query = "선풍기"
            val searchResults = (0..9).map {
                AutoCompleteDocument(
                    keyword = "$query $it",
                    suggestNgram = "$query $it",
                    frequency = it.toLong()
                )
            }

            val firstKeyword = "$query 0"
            val lastKeyword = "$query 9"

            // when
            whenever(autoCompleteRepository.findBySuggestWithPageable(anyString(), any()))
                .thenReturn(searchResults)
            val suggestions = autoCompleteService.getSuggestions(query)

            // then
            assertThat(suggestions).hasSize(10)
            assertThat(suggestions.first().keyword).isEqualTo(firstKeyword)
            assertThat(suggestions.last().keyword).isEqualTo(lastKeyword)
        }

        @Test
        @DisplayName("결과는 빈도수 내림차순으로 정렬되어야 한다")
        fun `결과가 정렬 순서에 맞게 반환`() {
            // given
            val query = "선풍기"

            // 데이터를 의도적으로 역순으로 생성
            val searchResults = (0..9).map {
                AutoCompleteDocument(
                    keyword = "$query $it",
                    suggestNgram = "$query $it",
                    frequency = it.toLong()
                )
            }
            val sortedResults = searchResults.asReversed()

            // when
            whenever(autoCompleteRepository.findBySuggestWithPageable(anyString(), any()))
                .thenReturn(sortedResults)

            val suggestions = autoCompleteService.getSuggestions(query)

            // then
            assertThat(suggestions).hasSize(10)
            assertThat(suggestions.first().keyword).isEqualTo(sortedResults.first().keyword)
            assertThat(suggestions.last().keyword).isEqualTo(sortedResults.last().keyword)
        }

        @Test
        @DisplayName("자동완성 목록이 없는 없는 경우 빈 목록을 반환")
        fun `검색어 자동완성 목록이 없는 없는 경우 빈 목록을 반환`() {
            // given
            val query = "존재하지않는검색어"
            val searchResults = listOf<AutoCompleteDocument>()
            searchResults.map { AutoCompleteResponse(it.keyword) }

            // when
            whenever(autoCompleteRepository.findBySuggestWithPageable(anyString(), any()))
                .thenReturn(searchResults)
            val suggestions = autoCompleteService.getSuggestions(query)

            // then
            assertThat(suggestions).hasSize(0)
        }

        @Test
        @DisplayName("검색어 비어있는 경우")
        fun `검색어가 비어있는 경우`() {
            // given
            val query = ""
            val searchResults = listOf<AutoCompleteDocument>()

            // when
            whenever(autoCompleteRepository.findBySuggestWithPageable(anyString(), any()))
                .thenReturn(searchResults)
            val suggestions = autoCompleteService.getSuggestions(query)

            // then
            assertThat(suggestions).hasSize(0)
        }
    }

}