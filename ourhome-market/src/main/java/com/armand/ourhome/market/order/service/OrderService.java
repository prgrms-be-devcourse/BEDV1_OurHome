package com.armand.ourhome.market.order.service;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.domain.OrderItem;
import com.armand.ourhome.market.order.dto.OrderItemRequest;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import com.armand.ourhome.market.order.exception.OrderNotFoundException;
import com.armand.ourhome.market.order.exception.UserNotFoundException;
import com.armand.ourhome.market.order.mapper.OrderMapper;
import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final DeliveryService deliveryService;
    private final OrderItemService orderItemService;

    // 주문 생성
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        // 유저 정보 조회
        Long userId = orderRequest.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        // 주문 아이템 생성
        List<OrderItem> orderItems = orderItemService.createOrderItems(orderRequest.getOrderItemRequests());

        // 배달 정보 생성
        UUID code = deliveryService.createDelivery();
        Delivery delivery = deliveryService.findByCode(code);

        // 주문 생성
        Order order = orderMapper.requestToEntity(orderRequest);
        order = Order.createOrder(order.getPaymentType(), user, delivery, orderItems);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.entityToResponse(savedOrder);
    }

    public OrderResponse lookUpOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::entityToResponse)
                .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
    }
}
