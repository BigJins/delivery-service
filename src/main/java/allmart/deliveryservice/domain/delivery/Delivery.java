package allmart.deliveryservice.domain.delivery;

import allmart.deliveryservice.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "deliveries",
        indexes = {
                @Index(name = "idx_deliveries_order_id", columnList = "order_id"),
                @Index(name = "idx_deliveries_buyer_id", columnList = "buyer_id")
        }
)
public class Delivery extends AbstractEntity {

    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @Column(name = "buyer_id", nullable = false, updatable = false)
    private Long buyerId;

    /** 주문 금액 — delivery.completed.v1 이벤트에 포함 */
    @Column(name = "total_amount", nullable = false, updatable = false)
    private long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryStatus status;

    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    /** DB에는 원본 저장, 이벤트 발행 시 마스킹 처리 */
    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "detail_address", nullable = false, length = 200)
    private String detailAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "delivered_at")
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
