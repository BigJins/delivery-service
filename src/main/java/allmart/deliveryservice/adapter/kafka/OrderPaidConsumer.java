package allmart.deliveryservice.adapter.kafka;

import allmart.deliveryservice.adapter.kafka.dto.OrderPaidMessage;
import allmart.deliveryservice.application.provided.DeliveryCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * order.paid.v1 이벤트 수신
 * 결제 완료된 주문에 대해 배송을 자동 생성
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPaidConsumer {

    private final ObjectMapper objectMapper;
    private final DeliveryCreator deliveryCreator;

    @KafkaListener(topics = "${kafka.topics.order-paid}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(String value) {
        log.info("order.paid.v1 수신: {}", value);
        try {
            // Debezium schema envelope 처리: {"schema":...,"payload":"<json>"} 형식 대응
            var root = objectMapper.readTree(value);
            String payload = root.has("payload") ? root.get("payload").asText() : value;

            OrderPaidMessage msg = objectMapper.readValue(payload, OrderPaidMessage.class);
            var addr = msg.deliveryAddress();
            deliveryCreator.create(
                    msg.orderId(),
                    msg.buyerId(),
                    msg.totalAmount(),
                    "",                 // receiverName — PII 보호로 이벤트 미포함
                    "",                 // receiverPhone — PII 보호로 이벤트 미포함
                    addr.zipCode(),
                    addr.roadAddress(),
                    addr.detailAddress()
            );
            log.info("배송 생성 완료: orderId={}", msg.orderId());
        } catch (Exception e) {
            log.warn("order.paid.v1 처리 실패, skip: {}", value, e);
        }
    }
}
