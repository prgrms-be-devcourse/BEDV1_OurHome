package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(OurHomeDomainConfig.class)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("status column은 기본값이 ACCEPTED이다.")
    public void save() {
        // Given
        User user = User.builder()
                .email("iyj6707@naver.com")
                .password("1234")
                .nickname("iyj6707")
                .build();

        Delivery delivery = Delivery.builder().build();

        Item soapItem = Item.builder()
                .price(1000)
                .name("soap")
                .imageUrl("")
                .description("clean")
                .imageUrl("imageurl")
                .company(new Company("samsung"))
                .category(Category.DAILY_NECESSITIES)
                .build();

        Item ramenItem = Item.builder()
                .price(1500)
                .name("ramen")
                .imageUrl("")
                .description("yummy")
                .imageUrl("imageurl")
                .category(Category.DAILY_NECESSITIES)
                .company(new Company("samyang"))
                .build();

        itemRepository.saveAll(List.of(ramenItem, soapItem));

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                .orderCount(2)
                .item(soapItem)
                .build());
        orderItems.add(OrderItem.builder()
                .orderCount(1)
                .item(ramenItem)
                .build());

        Order order = Order.createOrder(PaymentType.FUND_TRANSFER, "address", user, delivery, orderItems);

        deliveryRepository.save(delivery);
        userRepository.save(user);

        // When
        Order savedOrder = orderRepository.save(order);

        // Then
        assertThat(savedOrder.getPaymentType()).isEqualTo(PaymentType.FUND_TRANSFER);
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }
}