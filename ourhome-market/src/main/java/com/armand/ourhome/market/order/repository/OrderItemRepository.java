package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select exist (select i from OrderItem i " +
            "inner join Order o on i.order = o " +
            "where o.user = :userId and i.item = :itemId)")
    boolean existsOrderItemByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
