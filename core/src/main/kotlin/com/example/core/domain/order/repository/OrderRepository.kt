package com.example.core.domain.order.repository

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.order.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<Order, Long> {

    @Query(
        """
        SELECT new com.example.core.domain.order.dto.ProductSalesResponse(o.product.productId, COUNT(o))
        FROM Order o
        WHERE o.createdAt BETWEEN :startDate AND :endDate
        GROUP BY o.product.productId
    """
    )
    fun findSalesCountByPeriod(startDate: LocalDateTime, endDate: LocalDateTime): List<ProductSalesResponse>

}