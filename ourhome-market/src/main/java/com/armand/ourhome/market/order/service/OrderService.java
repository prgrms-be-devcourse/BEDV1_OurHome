package com.armand.ourhome.market.order.service;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.domain.Delivery;
import com.armand.ourhome.market.order.domain.OrderItem;
import com.armand.ourhome.market.order.dto.OrderItemRequest;
import com.armand.ourhome.market.order.dto.OrderRequest;
import com.armand.ourhome.market.order.dto.OrderResponse;
import com.armand.ourhome.market.order.exception.OrderNotFoundException;
import com.armand.ourhome.market.order.exception.UserNotFoundException;
import com.armand.ourhome.market.order.mapper.OrderMapper;
import com.armand.ourhome.market.order.domain.Order;
import com.armand.ourhome.market.order.repository.OrderRepository;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.domain.Wallet;
import com.armand.ourhome.market.voucher.exception.VoucherNotFoundException;
import com.armand.ourhome.market.voucher.exception.WalletNotFoundException;
import com.armand.ourhome.market.voucher.repository.VoucherRepository;
import com.armand.ourhome.market.voucher.repository.WalletRepository;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final VoucherRepository<Voucher> voucherRepository;
  private final WalletRepository walletRepository;

  private final DeliveryService deliveryService;
  private final OrderItemService orderItemService;

  @Transactional
  public OrderResponse createOrder(OrderRequest orderRequest) {

    // 유저 정보 조회
    Long userId = orderRequest.getUserId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId.toString()));

    // 바우처 사용
    Voucher voucher = null;
    if (orderRequest.getVoucherId() != null) {
      Long voucherId = orderRequest.getVoucherId();
      voucher = voucherRepository.findById(voucherId)
          .orElseThrow(() -> new VoucherNotFoundException(
              MessageFormat.format("등록된 바우처가 아닙니다. voucherId : {0}", voucherId)));
      Wallet wallet = walletRepository.findByUserIdAndVoucherId(userId, voucherId)
          .orElseThrow(() -> new WalletNotFoundException(
              MessageFormat.format(
                  "사용자(userId : {0})는 해당 바우처(voucherId : {1})를 갖고있지 않습니다.", userId, voucherId)
          ));
      walletRepository.delete(wallet);
    }

    // 주문 아이템 생성
    List<OrderItem> orderItems = orderItemService.createOrderItems(
        orderRequest.getOrderItemRequests());

    // 배달 정보 생성
    UUID code = deliveryService.createDelivery();
    Delivery delivery = deliveryService.findByCode(code);

    // 주문 생성
    Order order = Order.createOrder(orderRequest.getPaymentType(), orderRequest.getAddress(), user,
        delivery, orderItems, voucher);
    Order savedOrder = orderRepository.save(order);

    return orderMapper.entityToResponse(savedOrder);
  }

  @Transactional
  public OrderResponse lookUpOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .map(orderMapper::entityToResponse)
        .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));
  }

  @Transactional
  public OrderResponse deleteOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException(orderId.toString()));

    order.cancelOrder();

    return orderMapper.entityToResponse(order);
  }

}
