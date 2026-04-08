package allmart.deliveryservice.domain.delivery;

import allmart.deliveryservice.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends AbstractEntity {

    private Long orderId;

    private Long buyerId;

    /** 주문 금액 — delivery.completed.v1 이벤트에 포함 */
    private long totalAmount;

    private DeliveryStatus status;

    private String receiverName;

    /** DB에는 원본 저장, 이벤트 발행 시 마스킹 처리 */
    private String receiverPhone;

    private String zipCode;

    private String address;

    private String detailAddress;

    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    public static Delivery create(
            Long orderId,
            Long buyerId,
            long totalAmount,
            String receiverName,
            String receiverPhone,
            String zipCode,
            String address,
            String detailAddress
    ) {
        Delivery d = new Delivery();
        d.orderId = orderId;
        d.buyerId = buyerId;
        d.totalAmount = totalAmount;
        d.status = DeliveryStatus.PENDING;
        d.receiverName = receiverName;
        d.receiverPhone = receiverPhone;
        d.zipCode = zipCode;
        d.address = address;
        d.detailAddress = detailAddress;
        d.createdAt = LocalDateTime.now();
        return d;
    }

    /**
     * 배송 상태를 다음 단계로 전이한다.
     * 상태 머신 규칙을 위반하면 IllegalStateException 발생.
     */
    public void advance(DeliveryStatus newStatus) {
        if (!this.status.canTransitTo(newStatus)) {
            throw new IllegalStateException(
                    "잘못된 상태 전이: " + this.status + " → " + newStatus);
        }
        this.status = newStatus;
        if (newStatus == DeliveryStatus.DELIVERED) {
            this.deliveredAt = LocalDateTime.now();
        }
    }

    public boolean isDelivered() {
        return this.status == DeliveryStatus.DELIVERED;
    }
}