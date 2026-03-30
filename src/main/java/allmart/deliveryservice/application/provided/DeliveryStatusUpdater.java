package allmart.deliveryservice.application.provided;

import allmart.deliveryservice.domain.delivery.DeliveryStatus;

public interface DeliveryStatusUpdater {
    void advance(Long deliveryId, DeliveryStatus newStatus);
}
