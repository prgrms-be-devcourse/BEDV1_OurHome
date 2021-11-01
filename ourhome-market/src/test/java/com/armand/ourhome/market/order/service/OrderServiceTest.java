package com.armand.ourhome.market.order.service;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.domain.*;
import com.armand.ourhome.market.order.dto.DeliveryResponse;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import com.armand.ourhome.market.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrder() {

        // Given
        User user = User.builder()
                .email("iyj6707@naver.com")
                .password("1234")
                .nickname("iyj6707")
                .build();

        Delivery delivery = Delivery.builder()
                .code(UUID.randomUUID())
                .build();

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(1L)
                .build();

        Order order = Order.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .delivery(delivery)
                .build();

        given(deliveryService.findByCode(any())).willReturn(delivery);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(orderRepository.save(any())).willReturn(order);

        // When
        OrderResponse createdOrder = orderService.createOrder(orderRequest);

        // Then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
        assertThat(createdOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(createdOrder.getPaymentType()).isEqualTo(order.getPaymentType());
        assertThat(createdOrder.getDeliveryResponse()).isNotNull();
        assertThat(createdOrder.getDeliveryResponse().getCode()).isNotNull();
    }
}