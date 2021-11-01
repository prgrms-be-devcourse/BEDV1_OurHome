package com.armand.ourhome.market.order.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class UserResponse {

    private Long id;

    private String name;
}
