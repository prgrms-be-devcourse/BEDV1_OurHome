package com.armand.ourhome.market;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.PaymentType;
import com.armand.ourhome.market.order.dto.OrderItemRequest;
import com.armand.ourhome.market.order.dto.OrderRequest;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static User createUserWithoutAddress() {
        return User.builder()
                .email("iyj6707@naver.com")
                .password("1234")
                .nickname("iyj6707")
                .build();
    }

    public static User createUser() {
        return User.builder()
                .email("iyj6707@naver.com")
                .password("1234")
                .nickname("iyj6707")
                .address("Seoul city")
                .build();
    }

    public static Item createSoapItem() {
        return Item.builder()
                .price(1000)
                .name("soap")
                .imageUrl("example")
                .description("clean")
                .stockQuantity(4)
                .company(new Company("samsung"))
                .category(Category.DAILY_SUPPLIES)
                .build();
    }

    public static Item createRamenitem() {
        return Item.builder()
                .price(1500)
                .name("ramen")
                .imageUrl("example")
                .description("yummy")
                .stockQuantity(3)
                .company(new Company("samyang"))
                .category(Category.ETC)
                .build();
    }

    private static List<OrderItemRequest> createOrderItemRequests() {
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(2)
                .itemId(1L)
                .build());
        orderItemRequests.add(OrderItemRequest.builder()
                .orderCount(1)
                .itemId(2L)
                .build());

        return orderItemRequests;
    }

    public static OrderRequest createOrderRequest() {
        return OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(1L)
                .address("Seoul City")
                .orderItemRequests(createOrderItemRequests())
                .build();
    }

    public static OrderRequest createOrderRequestWithoutAddress() {
        return OrderRequest.builder()
                .paymentType(PaymentType.FUND_TRANSFER)
                .userId(1L)
                .orderItemRequests(createOrderItemRequests())
                .build();
    }
}
