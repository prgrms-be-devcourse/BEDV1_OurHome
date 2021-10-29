package com.armand.ourhome.market.order.dto;

import lombok.*;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class UserRequest {

    private Long id;

    private String name;
}
