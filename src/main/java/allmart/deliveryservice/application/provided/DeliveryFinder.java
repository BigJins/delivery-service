package allmart.deliveryservice.application.provided;

import allmart.deliveryservice.domain.delivery.Delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryFinder {
    Delivery findById(Long deliveryId);

    List<Delivery> findAll();

    Optional<Delivery> findByOrderId(Long orderId);
}
