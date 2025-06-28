package com.example.worker.schedule

import com.example.core.domain.order.dto.ProductSalesResponse
import com.example.core.domain.order.repository.OrderRepository
import com.example.core.domain.product.repository.ProductDocumentRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OrderSchedule(
    private val orderRepository: OrderRepository,
    private val productDocumentRepository: ProductDocumentRepository
) {

    private val logger = LoggerFactory.getLogger(OrderSchedule::class.java)

    companion object {
        private const val WINDOW_MINUTES = 1L
    }

    @Scheduled(cron = "0 * * * * *")
    fun updateSalesCountDaily() {
        logger.info("판매량 업데이트 스케줄 시작")
        val endTime = LocalDateTime.now()
        val startTime = endTime.minusMinutes(WINDOW_MINUTES)

        val salesData = orderRepository.findSalesCountByPeriod(startTime, endTime)
        if (salesData.isEmpty()) {
            logger.info("업데이트할 판매량 데이터가 없습니다.")
            return
        }

        updateProductSales(salesData)
    }

    private fun updateProductSales(salesData: List<ProductSalesResponse>) {
        try {
            productDocumentRepository.bulkUpdateSalesCount(salesData)
            logger.info("총 ${salesData.size}개 상품의 일일 판매량 업데이트 성공")
        } catch (e: Exception) {
            logger.error("판매량 업데이트 스케줄 중 오류 발생")
        }
    }

}