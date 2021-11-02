package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.domain.OrderStatus;
import com.armand.ourhome.market.order.domain.PaymentType;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderRequest {

    private PaymentType paymentType;

    private Long userId;

    private String address;

    private List<OrderItemRequest> orderItemRequests;

    @Builder
    public OrderRequest(PaymentType paymentType, Long userId, String address, List<OrderItemRequest> orderItemRequests) {

        this.paymentType = paymentType;
        this.userId = userId;
        this.address = address;
        this.orderItemRequests = orderItemRequests;
    }
}
