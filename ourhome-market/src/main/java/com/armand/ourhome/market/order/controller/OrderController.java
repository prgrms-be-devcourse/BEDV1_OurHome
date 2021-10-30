package com.armand.ourhome.market.order.controller;

import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import com.armand.ourhome.market.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody final OrderRequest orderRequest) {
        final OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> lookUpOrder(@Valid @PathVariable final Long orderId) {
        final OrderResponse orderResponse = orderService.lookUpOrder(orderId);
        return ResponseEntity.ok(orderResponse);
    }
}
