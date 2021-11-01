package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.market.order.domain.DeliveryStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DeliveryRequest {

    private Long id;

    private DeliveryStatus status;

    private UUID code;
}
