package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
