package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.domain.OrderStatus;
import com.armand.ourhome.market.order.domain.PaymentType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@EqualsAndHashCode
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {

    @NotNull
    private Long id;

    @NotBlank
    private String address;

    @NotNull
    private OrderStatus status;

    @NotNull
    private PaymentType paymentType;

    @NotNull
    private long totalPrice;

    @NotNull
    private DeliveryResponse deliveryResponse;
}
