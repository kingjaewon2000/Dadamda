package com.example.core.domain.log.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
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