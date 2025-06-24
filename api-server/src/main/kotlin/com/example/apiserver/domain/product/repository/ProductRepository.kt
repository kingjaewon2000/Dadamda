package com.example.apiserver.domain.product.repository

import com.example.apiserver.domain.product.entity.Product
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productId > :cursorId ORDER BY p.productId ASC LIMIT :pageSize")
    fun findNextPage(cursorId: Long, pageSize: Int): List<Product>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :productId")
    fun findByIdForUpdate(productId: Long): Product?

    /*
        검색용 쿼리
     */
    @Query(
        "SELECT p FROM Product p " +
                "WHERE p.name Like %:keyword% " +
                "ORDER BY p.productId DESC "
    )
    fun findNewest(keyword: String): List<Product>

    @Query(
        "SELECT p FROM Product p " +
                "LEFT JOIN Order o ON p.productId = o.product.productId " +
                "WHERE p.name Like %:keyword% " +
                "GROUP BY p.productId " +
                "ORDER BY count(o) DESC "
    )
    fun findBestSelling(keyword: String): List<Product>

}