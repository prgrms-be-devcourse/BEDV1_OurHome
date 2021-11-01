package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.domain.OrderStatus;
import com.armand.ourhome.market.order.domain.PaymentType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class OrderResponse {

    private Long id;

    private String address;

    private OrderStatus status;

    private PaymentType paymentType;

    private long totalPrice;

    private DeliveryResponse deliveryResponse;
}
