package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // limit 1로 하면 성능 더 좋지만, JPQL limit 지원안함
    @Query("select count(i) > 0 from OrderItem i " +
            "join i.order o " +
            "where o.user.id = :userId and i.item.id = :itemId")
    boolean existsOrderItemByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
