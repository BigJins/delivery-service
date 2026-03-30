package allmart.deliveryservice.application.required;

import allmart.deliveryservice.domain.delivery.Delivery;

public interface OutboxEventPublisher {
    void publishDeliveryCompleted(Delivery delivery);
}
