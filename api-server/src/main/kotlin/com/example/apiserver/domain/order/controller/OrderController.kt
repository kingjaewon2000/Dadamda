package com.example.apiserver.domain.order.controller

import com.example.apiserver.domain.order.dto.OrderCreateRequest
import com.example.apiserver.domain.order.dto.OrderIdResponse
import com.example.apiserver.domain.order.service.OrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping
    fun createOrder(@RequestBody request: OrderCreateRequest): OrderIdResponse {
        return orderService.createOrder(request)
    }

}