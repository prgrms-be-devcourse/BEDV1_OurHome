package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.market.order.domain.DeliveryStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeliveryResponse {

    private DeliveryStatus status;

    private UUID code;
}
