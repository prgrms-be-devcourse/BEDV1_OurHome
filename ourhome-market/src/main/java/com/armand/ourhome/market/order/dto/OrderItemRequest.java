package com.armand.ourhome.market.order.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemRequest {

    @NotNull
    private int orderCount;

    @NotNull
    private Long itemId;

    @Builder
    public OrderItemRequest(int orderCount, Long itemId) {
        this.orderCount = orderCount;
        this.itemId = itemId;
    }
}
