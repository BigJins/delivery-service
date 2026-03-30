package allmart.deliveryservice.domain;

import allmart.deliveryservice.domain.delivery.Delivery;
import allmart.deliveryservice.domain.delivery.DeliveryStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DeliveryTest {

    private Delivery createPendingDelivery() {
        return Delivery.create(1L, 42L, 50000L, "홍길동", "01012345678",
                "06234", "서울시 강남구 테헤란로 123", "101호");
    }

    @Test
    @DisplayName("배송 생성 시 초기 상태는 PENDING이다")
    void create_initialStatusIsPending() {
        Delivery delivery = createPendingDelivery();

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.PENDING);
        assertThat(delivery.getOrderId()).isEqualTo(1L);
        assertThat(delivery.getDeliveredAt()).isNull();
    }

    @Test
    @DisplayName("PENDING → PICKED_UP 정상 전이된다")
    void advance_pendingToPickedUp() {
        Delivery delivery = createPendingDelivery();

        delivery.advance(DeliveryStatus.PICKED_UP);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.PICKED_UP);
    }

    @Test
    @DisplayName("PICKED_UP → IN_TRANSIT → DELIVERED 순서로 전이된다")
    void advance_fullDeliveryFlow() {
        Delivery delivery = createPendingDelivery();

        delivery.advance(DeliveryStatus.PICKED_UP);
        delivery.advance(DeliveryStatus.IN_TRANSIT);
        delivery.advance(DeliveryStatus.DELIVERED);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        assertThat(delivery.isDelivered()).isTrue();
        assertThat(delivery.getDeliveredAt()).isNotNull();
    }

    @Test
    @DisplayName("PENDING에서 DELIVERED로 직접 전이하면 예외가 발생한다")
    void advance_invalidTransition_throws() {
        Delivery delivery = createPendingDelivery();

        assertThatThrownBy(() -> delivery.advance(DeliveryStatus.DELIVERED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잘못된 상태 전이");
    }

    @Test
    @DisplayName("DELIVERED 상태에서 추가 전이하면 예외가 발생한다")
    void advance_fromDelivered_throws() {
        Delivery delivery = createPendingDelivery();
        delivery.advance(DeliveryStatus.PICKED_UP);
        delivery.advance(DeliveryStatus.IN_TRANSIT);
        delivery.advance(DeliveryStatus.DELIVERED);

        assertThatThrownBy(() -> delivery.advance(DeliveryStatus.CANCELED))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("IN_TRANSIT에서 CANCELED로 전이할 수 있다")
    void advance_inTransitToCanceled() {
        Delivery delivery = createPendingDelivery();
        delivery.advance(DeliveryStatus.PICKED_UP);
        delivery.advance(DeliveryStatus.IN_TRANSIT);

        delivery.advance(DeliveryStatus.CANCELED);

        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.CANCELED);
        assertThat(delivery.isDelivered()).isFalse();
    }
}
