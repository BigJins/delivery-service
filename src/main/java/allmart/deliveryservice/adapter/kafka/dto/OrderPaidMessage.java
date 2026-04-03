package allmart.deliveryservice.adapter.kafka.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * order.paid.v1 Kafka 메시지 DTO
 * 수령인명/전화번호 미포함 — order-service가 PII 보호를 위해 이벤트에서 제외
 */
public record OrderPaidMessage(
        Long orderId,
        String tossOrderId,
        Long buyerId,
        long totalAmount,
        DeliveryAddressDto deliveryAddress,
        MartDto mart,
        List<OrderLineDto> orderLines,
        LocalDateTime paidAt
) {

    public record DeliveryAddressDto(
            String zipCode,
            String roadAddress,
            String detailAddress
    ) {}

    public record MartDto(
            Long martId,
            String martName
    ) {}

    public record OrderLineDto(
            Long productId,
            String productName,
            int quantity,
            long unitPrice
    ) {}
}
