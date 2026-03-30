package allmart.deliveryservice.application.required;

import allmart.deliveryservice.domain.event.OutboxEvent;
import org.springframework.data.repository.Repository;

public interface OutboxRepository extends Repository<OutboxEvent, Long> {
    OutboxEvent save(OutboxEvent outboxEvent);
}
