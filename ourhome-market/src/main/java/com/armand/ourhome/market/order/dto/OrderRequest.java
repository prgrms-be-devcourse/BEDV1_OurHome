package com.armand.ourhome.market.order.dto;

import com.armand.ourhome.market.order.domain.PaymentType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {

    @NotNull
    private PaymentType paymentType;

    @NotNull
    private Long userId;

    // null이면 사용자 주소로 생성
    private String address;

    @NotNull
    private List<OrderItemRequest> orderItemRequests;

    // nulll이면 바우처를 사용하지 않은 것
    private Long voucherId;

    @Builder
    public OrderRequest(PaymentType paymentType, Long userId, String address, List<OrderItemRequest> orderItemRequests, Long voucherId) {
        this.paymentType = paymentType;
        this.userId = userId;
        this.address = address;
        this.orderItemRequests = orderItemRequests;
        this.voucherId = voucherId;
    }

}
