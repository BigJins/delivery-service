package allmart.deliveryservice.application.provided;

import allmart.deliveryservice.domain.delivery.Delivery;

public interface DeliveryCreator {
    Delivery create(
            Long orderId,
            Long buyerId,
            long totalAmount,
            String receiverName,
            String receiverPhone,
            String zipCode,
            String address,
            String detailAddress
    );
}
