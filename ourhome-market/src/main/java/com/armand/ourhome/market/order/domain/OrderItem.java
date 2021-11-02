package com.armand.ourhome.market.order.domain;

import com.armand.ourhome.domain.item.domain.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_count")
    private int orderCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Builder
    public OrderItem(int orderCount, Item item) {
        this.orderCount = orderCount;
        this.item = item;
    }

    public static OrderItem createOrderItem(int orderCount, Item item) {
        item.removeStockQuantity(orderCount);

        return OrderItem.builder()
                .orderCount(orderCount)
                .item(item)
                .build();
    }

    public long getPriceOfItems() {
        return (long) orderCount * item.getPrice();
    }

    public void updateOrder(Order order) {
        if (this.order != null) {
            this.order.removeOrderItem(this);
        }

        this.order = order;
    }
}
