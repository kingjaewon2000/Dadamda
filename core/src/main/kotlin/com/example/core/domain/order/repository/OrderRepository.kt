package com.example.core.domain.order.repository

import com.example.core.domain.order.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>