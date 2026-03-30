package allmart.deliveryservice.adapter.kafka;

import allmart.deliveryservice.adapter.kafka.dto.OrderPaidMessage;
import allmart.deliveryservice.application.provided.DeliveryCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * order.paid.v1 이벤트 수신
 * 결제 완료된 주문에 대해 배송을 자동 생성
 */
@Profile("local")
@Component
@RequiredArgsConstructor
@Log4j2
public class OrderPaidConsumer {

    private final ObjectMapper objectMapper;
    private final DeliveryCreator deliveryCreator;

    @KafkaListener(topics = "${kafka.topics.order-paid}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(String value) {
        log.info("order.paid.v1 수신: {}", value);
        try {
            OrderPaidMessage msg = objectMapper.readValue(value, OrderPaidMessage.class);
            var addr = msg.shippingAddress();
            deliveryCreator.create(
                    msg.orderId(),
                    msg.buyerId(),
                    msg.totalAmount(),
                    addr.receiverName(),
                    addr.receiverPhone(),  // order-service에서 이미 마스킹된 값
                    addr.zipCode(),
                    addr.address(),
                    addr.detailAddress()
            );
            log.info("배송 생성 완료: orderId={}", msg.orderId());
        } catch (Exception e) {
            log.warn("order.paid.v1 처리 실패, skip: {}", value, e);
        }
    }
}
