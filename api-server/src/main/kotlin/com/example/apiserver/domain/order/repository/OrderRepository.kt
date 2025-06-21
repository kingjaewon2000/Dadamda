package com.example.apiserver.domain.order.repository

import com.example.apiserver.domain.order.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>