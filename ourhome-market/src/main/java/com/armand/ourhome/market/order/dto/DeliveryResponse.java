package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.market.order.domain.DeliveryStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DeliveryResponse {

    private DeliveryStatus status;

    private UUID code;
}
