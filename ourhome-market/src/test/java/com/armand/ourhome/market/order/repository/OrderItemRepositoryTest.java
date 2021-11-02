package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.OrderItem;
import com.armand.ourhome.market.order.domain.PaymentType;
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
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("사용자가 구매한 상품인지 확인 가능하다")
    public void testExist() throws Exception {
        //given
        User user = User.builder()
                .email("iyj6707@naver.com")
                .password("1234")
                .nickname("iyj6707")
                .build();

        Delivery delivery = Delivery.builder().build();

        Item soapItem = Item.builder()
                .price(1000)
                .name("soap")
                .stockQuantity(1000)
                .imageUrl("sl;fk;sdflks;fksdl;")
                .description("clean")
                .company(new Company("samsung"))
                .category(Category.DAILY_NECESSITIES)
                .build();

        Item ramenItem = Item.builder()
                .price(1500)
                .stockQuantity(1000)
                .name("ramen")
                .imageUrl("sldfjdlkfjsdlfjdklsf")
                .description("yummy")
                .company(new Company("samyang"))
                .category(Category.DAILY_NECESSITIES)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                .orderCount(2)
                .item(soapItem)
                .build());
        orderItems.add(OrderItem.builder()
                .orderCount(1)
                .item(ramenItem)
                .build());

        Order order = Order.createOrder(PaymentType.CREDIT_CARD, "address", user, delivery, orderItems);

        itemRepository.saveAll(List.of(ramenItem, soapItem));
        deliveryRepository.save(delivery);
        userRepository.save(user);

        //when
        Order savedOrder = orderRepository.save(order);


        //then
        boolean exist1 = orderItemRepository.existsOrderItemByUserIdAndItemId(user.getId(), ramenItem.getId());
        boolean exist2 = orderItemRepository.existsOrderItemByUserIdAndItemId(user.getId(), 1000L);
        assertThat(exist1).isTrue();
        assertThat(exist2).isFalse();
    }


}