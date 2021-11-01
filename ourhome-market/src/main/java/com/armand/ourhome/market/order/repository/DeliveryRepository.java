package com.armand.ourhome.market.order.repository;

import com.armand.ourhome.market.order.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByCode(UUID deliveryCode);
}
