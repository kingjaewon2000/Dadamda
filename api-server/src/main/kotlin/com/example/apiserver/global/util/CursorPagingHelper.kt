package com.example.apiserver.global.util

import com.example.apiserver.domain.product.dto.CursorPageResponse

object CursorPagingHelper {

    fun <T, C> getCursorPage(
        content: List<T>,
        pageSize: Int,
        cursorExtractor: (T) -> C
    ): CursorPageResponse<T, C> {
        val hasNext = content.size > pageSize
        val subContent = if (hasNext) content.subList(0, pageSize) else content

        val nextCursor = if (hasNext) {
            cursorExtractor(subContent.last())
        } else {
            null
        }

        return CursorPageResponse(
            content = subContent,
            hasNext = hasNext,
            nextCursor = nextCursor
        )
    }

}