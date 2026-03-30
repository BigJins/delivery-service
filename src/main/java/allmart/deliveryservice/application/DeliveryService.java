package allmart.deliveryservice.application;

import allmart.deliveryservice.application.provided.DeliveryCreator;
import allmart.deliveryservice.application.provided.DeliveryFinder;
import allmart.deliveryservice.application.provided.DeliveryStatusUpdater;
import allmart.deliveryservice.application.required.DeliveryRepository;
import allmart.deliveryservice.application.required.OutboxEventPublisher;
import allmart.deliveryservice.domain.delivery.Delivery;
import allmart.deliveryservice.domain.delivery.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService implements DeliveryCreator, DeliveryStatusUpdater, DeliveryFinder {

    private final DeliveryRepository deliveryRepository;
    private final OutboxEventPublisher outboxEventPublisher;

    @Override
    public Delivery create(
            Long orderId,
            Long buyerId,
            long totalAmount,
            String receiverName,
            String receiverPhone,
            String zipCode,
            String address,
            String detailAddress
    ) {
        Delivery delivery = Delivery.create(orderId, buyerId, totalAmount,
                receiverName, receiverPhone, zipCode, address, detailAddress);
        return deliveryRepository.save(delivery);
    }

    @Override
    public void advance(Long deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다: " + deliveryId));

        delivery.advance(newStatus);

        if (delivery.isDelivered()) {
            // DELIVERED 전이 시 delivery.completed.v1 Outbox 저장 (같은 트랜잭션)
            outboxEventPublisher.publishDeliveryCompleted(delivery);
            log.info("배달 완료 Outbox 이벤트 저장: deliveryId={}, orderId={}",
                    delivery.getId(), delivery.getOrderId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Delivery findById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("배송을 찾을 수 없습니다: " + deliveryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findAll() {
        return deliveryRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Delivery> findByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }
}
