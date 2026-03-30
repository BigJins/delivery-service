package allmart.deliveryservice.adapter.webapi;

import allmart.deliveryservice.adapter.webapi.dto.DeliveryResponse;
import allmart.deliveryservice.adapter.webapi.dto.DeliveryStatusRequest;
import allmart.deliveryservice.application.provided.DeliveryFinder;
import allmart.deliveryservice.application.provided.DeliveryStatusUpdater;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryApi {

    private final DeliveryStatusUpdater deliveryStatusUpdater;
    private final DeliveryFinder deliveryFinder;

    /**
     * 배송 상태 전이
     * 기사 앱에서 호출: PENDING → PICKED_UP → IN_TRANSIT → DELIVERED
     */
    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryResponse> updateStatus(
            @PathVariable Long deliveryId,
            @RequestBody @Valid DeliveryStatusRequest request
    ) {
        deliveryStatusUpdater.advance(deliveryId, request.status());
        return ResponseEntity.ok(DeliveryResponse.from(deliveryFinder.findById(deliveryId)));
    }

    /** 배송 목록 조회 */
    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> findAll() {
        List<DeliveryResponse> list = deliveryFinder.findAll().stream()
                .map(DeliveryResponse::from).toList();
        return ResponseEntity.ok(list);
    }

    /** 주문 ID로 배송 조회 */
    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<DeliveryResponse> findByOrderId(@PathVariable Long orderId) {
        return deliveryFinder.findByOrderId(orderId)
                .map(DeliveryResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 배송 상세 조회
     */
    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryResponse> find(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(DeliveryResponse.from(deliveryFinder.findById(deliveryId)));
    }
}
