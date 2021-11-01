package com.armand.ourhome.market.order.service;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.market.order.domain.OrderItem;
import com.armand.ourhome.market.order.dto.OrderItemRequest;
import com.armand.ourhome.market.order.exception.ItemNotFoundException;
import com.armand.ourhome.market.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {

    final ItemRepository itemRepository;

    final OrderItemRepository orderItemRepository;

    public List<OrderItem> createOrderItems(List<OrderItemRequest> orderItemRequests) {

        List<OrderItem> orderItems = new ArrayList<>();

        for (var orderItemRequest : orderItemRequests) {
            Long itemId = orderItemRequest.getItemId();

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNotFoundException(itemId.toString()));

            orderItems.add(OrderItem.createOrderItem(orderItemRequest.getOrderCount(), item));
        }

        return orderItems;
    }
}
