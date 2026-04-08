package allmart.deliveryservice.domain.event;

import allmart.deliveryservice.config.SnowflakeGenerated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxEvent {

    @Id
    @SnowflakeGenerated
    private Long id;

    private String eventType;

    private String aggregateId;

    private String aggregateType;

    private String payload;

    private LocalDateTime createdAt;

    public static OutboxEvent create(
            String eventType,
            String aggregateType,
            String aggregateId,
            String payload
    ) {
        OutboxEvent event = new OutboxEvent();
        event.eventType = eventType;
        event.aggregateType = aggregateType;
        event.aggregateId = aggregateId;
        event.payload = payload;
        event.createdAt = LocalDateTime.now();
        return event;
    }
}