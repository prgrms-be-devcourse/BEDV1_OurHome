package com.armand.ourhome.market.order.mapper;

import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {

    @Mapping(source = "delivery", target = "deliveryResponse")
    @Mapping(target = "totalPrice", expression = "java(order.getTotalPrice())")
    OrderResponse entityToResponse(Order order);
}
