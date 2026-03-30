package allmart.deliveryservice.adapter.webapi.dto;

import allmart.deliveryservice.domain.delivery.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record DeliveryStatusRequest(
        @NotNull(message = "status는 필수입니다")
        DeliveryStatus status
) {}
