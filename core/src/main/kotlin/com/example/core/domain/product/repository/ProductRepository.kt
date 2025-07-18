package com.example.core.domain.product.repository

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.product.entity.Product
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.stream.Stream

interface ProductRepository : JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :productId")
    fun findByIdForUpdate(productId: Long): Product?

    /*
     *  검색용 쿼리
     */
    @Query(
        value = "SELECT p FROM Product p WHERE p.name Like %:keyword% ORDER BY p.productId DESC",
        countQuery = "SELECT count(p) FROM Product p WHERE p.name LIKE %:keyword%"
    )
    fun findNewest(keyword: String, pageable: Pageable): Page<Product>

    @Query(
        value = "SELECT p FROM Product p " +
                "LEFT JOIN Order o ON p.productId = o.product.productId " +
                "WHERE p.name Like %:keyword% " +
                "GROUP BY p.productId " +
                "ORDER BY count(o) DESC ",
        countQuery = "SELECT count(o) FROM Product p LEFT JOIN Order o on p.productId = o.product.productId WHERE p.name like %:keyword% GROUP BY p.productId"
    )
    fun findBestSelling(keyword: String, pageable: Pageable): Page<Product>

    /*
     *  배치용 쿼리
     */
    @Query(
        """
        SELECT new com.example.core.domain.order.dto.ProductSalesResponse(p.productId, p.name, p.price, p.stockQuantity, COALESCE(SUM(o.quantity), 0L), p.createdAt)
        FROM Product p left JOIN Order o ON p.productId = o.product.productId
        GROUP BY p.productId
    """
    )
    fun findAllWithSalesCountAsStream(): Stream<ProductSalesResponse>

}