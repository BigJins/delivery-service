package allmart.deliveryservice.adapter.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * order.paid.v1 Kafka 메시지 DTO
 */
public record OrderPaidMessage(
        Long orderId,
        String tossOrderId,
        Long buyerId,
        long totalAmount,
        ShippingAddressDto shippingAddress,
        List<OrderLineDto> orderLines,
        LocalDateTime paidAt
) {

    public record ShippingAddressDto(
            String zipCode,
            String address,
            String detailAddress,
            String receiverName,
            String receiverPhone
    ) {}

    public record OrderLineDto(
            Long productId,
            String productName,
            int quantity,
            long unitPrice
    ) {}
}
