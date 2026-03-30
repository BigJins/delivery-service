package allmart.deliveryservice.application.required;

import allmart.deliveryservice.domain.delivery.Delivery;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends Repository<Delivery, Long> {
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(Long id);

    Optional<Delivery> findByOrderId(Long orderId);

    List<Delivery> findAllByOrderByCreatedAtDesc();
}
