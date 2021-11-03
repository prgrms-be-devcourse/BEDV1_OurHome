package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "select count(*) > 0 from order_item i " +
            "join orders o on i.order_id = o.id " +
            "where o.user_id = :userId and i.item_id = :itemId limit 1", nativeQuery = true)
    boolean existsOrderItemByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
