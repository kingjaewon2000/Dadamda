package com.example.core.domain.product.repository

import com.example.core.domain.order.dto.ProductSalesResponse

interface CustomProductDocumentRepository {

    fun bulkUpdateSalesCount(salesResponses: List<ProductSalesResponse>)

}