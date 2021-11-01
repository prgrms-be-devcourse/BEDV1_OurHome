package com.armand.ourhome.market.order.mapper;

import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {

    @Mapping(target = "user", ignore = true)
    Order requestToEntity(OrderRequest orderRequest);

    @Mapping(source = "delivery", target = "deliveryResponse")
    OrderResponse entityToResponse(Order order);
}