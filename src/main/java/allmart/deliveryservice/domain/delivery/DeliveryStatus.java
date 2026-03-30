package allmart.deliveryservice.domain.delivery;

/**
 * 배송 상태 머신
 * PENDING → PICKED_UP → IN_TRANSIT → DELIVERED
 * IN_TRANSIT → CANCELED (예외적 취소)
 */
public enum DeliveryStatus {

    PENDING {
        @Override
        public boolean canTransitTo(DeliveryStatus next) {
            return next == PICKED_UP;
        }
    },
    PICKED_UP {
        @Override
        public boolean canTransitTo(DeliveryStatus next) {
            return next == IN_TRANSIT;
        }
    },
    IN_TRANSIT {
        @Override
        public boolean canTransitTo(DeliveryStatus next) {
            return next == DELIVERED || next == CANCELED;
        }
    },
    DELIVERED {
        @Override
        public boolean canTransitTo(DeliveryStatus next) {
            return false; // 터미널 상태
        }
    },
    CANCELED {
        @Override
        public boolean canTransitTo(DeliveryStatus next) {
            return false; // 터미널 상태
        }
    };

    public boolean canTransitTo(DeliveryStatus next) {
        return false;
    }
}
