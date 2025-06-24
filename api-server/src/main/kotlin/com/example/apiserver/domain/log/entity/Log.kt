package com.example.apiserver.domain.log.entity

import com.example.apiserver.domain.search.dto.ProductSortBy
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "logs")
@EntityListeners(AuditingEntityListener::class)
class Log(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var logId: Long = 0L,

    @Column
    @Enumerated(EnumType.STRING)
    val sortBy: ProductSortBy,

    @Column
    val keyword: String,

    @Column
    val length: Int,
) {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    lateinit var loggedAt: LocalDateTime
        private set

}