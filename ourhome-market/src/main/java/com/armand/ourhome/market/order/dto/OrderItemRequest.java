package com.armand.ourhome.market.order.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemRequest {

    private int orderCount;

    private Long itemId;

    @Builder
    public OrderItemRequest(int orderCount, Long itemId) {
        this.orderCount = orderCount;
        this.itemId = itemId;
    }
}
