package com.example.core.domain.product.repository

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.product.document.ProductDocument
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.document.Document
import org.springframework.data.elasticsearch.core.query.ScriptType
import org.springframework.data.elasticsearch.core.query.UpdateQuery
import org.springframework.stereotype.Repository

@Repository
class ProductDocumentRepositoryImpl(
    private val elasticsearchOperations: ElasticsearchOperations,
) : CustomProductDocumentRepository {


    override fun bulkUpdateSalesCount(salesResponses: List<ProductSalesResponse>) {
        if (salesResponses.isEmpty()) {
            return
        }

        val updateQueries = salesResponses.map { response ->
            val script = "ctx._source.salesCount = params.newSalesCount"
            val params = mapOf("newSalesCount" to response.salesCount)

            UpdateQuery.builder(response.productId.toString())
                .withScript(script)
                .withScriptType(ScriptType.INLINE)
                .withParams(params)
                .withScriptedUpsert(true)
                .withUpsert(
                    Document.from(
                        mapOf(
                            "id" to response.productId.toString(),
                            "salesCount" to response.salesCount
                        )
                    )
                )
                .build()
        }

        elasticsearchOperations.bulkUpdate(updateQueries, ProductDocument::class.java)
    }

}