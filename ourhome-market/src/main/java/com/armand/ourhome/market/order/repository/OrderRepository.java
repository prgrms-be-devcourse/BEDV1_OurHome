package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.market.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
