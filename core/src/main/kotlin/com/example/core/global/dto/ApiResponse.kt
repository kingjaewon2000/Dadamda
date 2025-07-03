package com.example.core.global.dto

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T
) {

    companion object {
        private const val SUCCESS_CODE = 200
        private const val SUCCESS_MESSAGE = "success"

        private const val CREATED_CODE = 201
        private const val CREATED_MESSAGE = "created"

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                code = SUCCESS_CODE,
                message = SUCCESS_MESSAGE,
                data = data
            )
        }

        fun <T> create(data: T): ApiResponse<T> {
            return ApiResponse(
                code = CREATED_CODE,
                message = CREATED_MESSAGE,
                data = data
            )
        }
    }

}