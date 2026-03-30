package allmart.deliveryservice.adapter.webapi.dto;

import allmart.deliveryservice.domain.delivery.Delivery;
import allmart.deliveryservice.domain.delivery.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponse(
        Long deliveryId,
        Long orderId,
        Long buyerId,
        DeliveryStatus status,
        String receiverName,
        String zipCode,
        String address,
        String detailAddress,
        LocalDateTime createdAt,
        LocalDateTime deliveredAt
) {
    public static DeliveryResponse from(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getBuyerId(),
                delivery.getStatus(),
                delivery.getReceiverName(),
                delivery.getZipCode(),
                delivery.getAddress(),
                delivery.getDetailAddress(),
                delivery.getCreatedAt(),
                delivery.getDeliveredAt()
        );
    }
}
