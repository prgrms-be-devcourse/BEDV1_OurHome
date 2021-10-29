package com.armand.ourhome.market.order.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class OrderItemRequest {

    private int orderCount;

    private Long itemId;
}
