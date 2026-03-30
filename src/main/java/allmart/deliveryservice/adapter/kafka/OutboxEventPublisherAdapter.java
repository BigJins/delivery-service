package allmart.deliveryservice.adapter.kafka;

import allmart.deliveryservice.adapter.kafka.dto.DeliveryCompletedPayload;
import allmart.deliveryservice.application.required.OutboxEventPublisher;
import allmart.deliveryservice.application.required.OutboxRepository;
import allmart.deliveryservice.domain.delivery.Delivery;
import allmart.deliveryservice.domain.event.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

/**
 * OutboxEventPublisher 구현체
 * Delivery → JSON 직렬화 → outbox_event 테이블 저장
 * 호출자의 트랜잭션 내에서 실행됨 (Outbox 패턴 핵심)
 */
@Service
@RequiredArgsConstructor
public class OutboxEventPublisherAdapter implements OutboxEventPublisher {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void publishDeliveryCompleted(Delivery delivery) {
        try {
            String json = objectMapper.writeValueAsString(DeliveryCompletedPayload.from(delivery));
            outboxRepository.save(OutboxEvent.create(
                    "delivery.completed.v1",
                    "delivery",
                    String.valueOf(delivery.getId()),
                    json
            ));
        } catch (Exception e) {
            throw new IllegalStateException(
                    "delivery.completed.v1 이벤트 직렬화 실패: deliveryId=" + delivery.getId(), e);
        }
    }
}
