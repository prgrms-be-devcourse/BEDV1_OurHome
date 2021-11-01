package com.armand.ourhome.market.order.service;

import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.exception.DeliveryNotFoundException;
import com.armand.ourhome.market.order.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public UUID createDelivery() {
        Delivery delivery = Delivery.newInstance();

        return deliveryRepository.save(delivery).getCode();
    }

    public Delivery findByCode(UUID deliveryCode) {
        return deliveryRepository.findByCode(deliveryCode)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryCode.toString()));
    }
}
