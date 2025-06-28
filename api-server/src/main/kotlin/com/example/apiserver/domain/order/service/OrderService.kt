package com.example.apiserver.domain.order.service

import com.example.apiserver.domain.order.dto.OrderCreateRequest
import com.example.apiserver.domain.order.dto.OrderIdResponse
import com.example.core.domain.order.repository.OrderRepository
import com.example.core.domain.product.repository.ProductRepository
import com.example.core.global.exception.ApiException
import com.example.core.global.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) {

    @Transactional
    fun createOrder(request: OrderCreateRequest): OrderIdResponse {
        val product = productRepository.findByIdForUpdate(request.productId)
            ?: throw ApiException(ErrorCode.PRODUCT_NOT_FOUND)

        product.decreaseStock(request.quantity)

        val order = orderRepository.save(request.toEntity(product))

        return OrderIdResponse(order.orderId)
    }

}