package allmart.deliveryservice.adapter.kafka.dto;

import allmart.deliveryservice.domain.delivery.Delivery;

import java.time.LocalDateTime;

/**
 * delivery.status.v1 Kafka 이벤트 페이로드
 * 모든 배송 상태 변경 시 발행 → order-query-service가 deliveryStatus 업데이트
 */
public record DeliveryStatusPayload(
        Long deliveryId,
        Long orderId,
        Long buyerId,
        String status,
        LocalDateTime updatedAt
) {

    public static DeliveryStatusPayload from(Delivery delivery) {
        return new DeliveryStatusPayload(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getBuyerId(),
                delivery.getStatus().name(),
                LocalDateTime.now()
        );
    }
}