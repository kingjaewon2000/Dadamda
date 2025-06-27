package com.example.core.domain.product.repository

import com.example.core.domain.product.document.ProductDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ProductDocumentRepository : ElasticsearchRepository<ProductDocument, String>