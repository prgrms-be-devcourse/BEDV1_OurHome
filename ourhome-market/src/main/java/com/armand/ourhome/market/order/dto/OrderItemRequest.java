package com.armand.ourhome.market.order.dto;


import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderItemRequest {

    private int orderCount;

    private Long itemId;

    @Builder
    public OrderItemRequest(int orderCount, Long itemId) {
        this.orderCount = orderCount;
        this.itemId = itemId;
    }
}
