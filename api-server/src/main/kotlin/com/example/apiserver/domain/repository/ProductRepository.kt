package com.example.apiserver.domain.repository

import com.example.apiserver.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.id > :cursorId ORDER BY p.id ASC LIMIT :limit")
    fun findNextPage(cursorId: Long?, limit: Int): List<Product>

}