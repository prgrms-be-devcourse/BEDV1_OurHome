package com.armand.ourhome.market.order.domain;

import com.armand.ourhome.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    public static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.ACCEPTED;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(PaymentType paymentType, User user, Delivery delivery, List<OrderItem> orderItems) {
        this.paymentType = paymentType;
        this.user = user;
        this.delivery = delivery;
        this.orderItems = orderItems;
    }

    static public Order createOrder(PaymentType paymentType, User user, Delivery delivery, List<OrderItem> orderItems) {
        return Order.builder()
                .paymentType(paymentType)
                .user(user)
                .delivery(delivery)
                .orderItems(orderItems)
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.status = this.status == null ? OrderStatus.ACCEPTED : this.status;
    }

    public long getTotalPrice() {
        long totalPrice = 0;
        for (OrderItem orderItem: orderItems) {
            totalPrice += orderItem.getPriceOfItems();
        }
        return totalPrice;
    }
}
