package com.example.apiserver.domain.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Table
@Entity
@EntityListeners(AuditingEntityListener::class)
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false)
    var name: String,

    @Column
    val description: String,

    @Column
    val price: Int,

    @Column
    val stock: Int
) {

    @Column(nullable = false, updatable = false)
    @CreatedDate
    lateinit var createdAt: LocalDateTime
        private set

    @Column(nullable = false)
    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

}