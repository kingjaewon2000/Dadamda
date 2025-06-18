package com.example.apiserver.global.exception

import com.example.core.global.exception.ApiErrorResponse
import com.example.core.global.exception.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    message = e.message ?: "에러 메시지가 존재하지 않습니다."
                )
            )
    }

    @ExceptionHandler(ApiException::class)
    fun apiException(e: ApiException): ResponseEntity<ApiErrorResponse> {
        val errorCode = e.errorCode

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiErrorResponse(
                    status = errorCode.status.value(),
                    message = errorCode.message
                )
            )
    }

}