package allmart.deliveryservice.adapter.kafka.dto;

import allmart.deliveryservice.domain.delivery.Delivery;

import java.time.LocalDateTime;

/**
 * delivery.completed.v1 Kafka 이벤트 페이로드
 */
public record DeliveryCompletedPayload(
        Long deliveryId,
        Long orderId,
        Long buyerId,
        long totalAmount,
        LocalDateTime completedAt
) {

    public static DeliveryCompletedPayload from(Delivery delivery) {
        return new DeliveryCompletedPayload(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getBuyerId(),
                delivery.getTotalAmount(),
                delivery.getDeliveredAt()
        );
    }
}
