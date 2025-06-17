package com.example.apiserver.domain.repository

import com.example.apiserver.domain.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>