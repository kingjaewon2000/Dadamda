package com.example.apiserver.domain.product.repository

import com.example.apiserver.domain.product.entity.Product
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productId > :cursorId ORDER BY p.productId ASC LIMIT :limit")
    fun findNextPage(cursorId: Long?, limit: Int): List<Product>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :productId")
    fun findByIdForUpdate(productId: Long): Product?

}