package com.armand.ourhome.market.order.domain;

import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.order.exception.DeliveryAlreadyStartedException;
import com.armand.ourhome.market.voucher.exception.VoucherCannotUseException;
import com.armand.ourhome.market.voucher.domain.Voucher;
import java.text.MessageFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

	public static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.ACCEPTED;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type", nullable = false)
	private PaymentType paymentType;

	@Column(name = "address", nullable = false)
	private String address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id", nullable = false)
	private Delivery delivery;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voucher_id")
	private Voucher voucher;

	@Builder
	public Order(PaymentType paymentType, String address, User user, Delivery delivery,
		List<OrderItem> orderItems, Voucher voucher) {
		this.paymentType = paymentType;
		this.address = address;
		this.user = user;
		this.delivery = delivery;
		this.orderItems = orderItems == null ? new ArrayList<>() : orderItems;
		this.voucher = voucher;
	}

	public static Order createOrder(PaymentType paymentType, String address, User user,
		Delivery delivery, List<OrderItem> orderItems, Voucher voucher) {

		Order order = Order.builder()
			.paymentType(paymentType)
			.address(address)
			.user(user)
			.delivery(delivery)
			.voucher(voucher)
			.build();

		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}

		return order;
	}

	//== 연관관계 편의 메서드 ==//
	public void addOrderItem(OrderItem orderItem) {
		orderItem.updateOrder(this);
		orderItems.add(orderItem);
	}

	public void removeOrderItem(OrderItem orderItem) {
		orderItems.remove(orderItem);
	}

	@PrePersist
	public void prePersist() {
		this.status = this.status == null ? OrderStatus.ACCEPTED : this.status;
		this.address = this.address == null ? user.getAddress() : this.address;
	}

	public void updateStatus(OrderStatus orderStatus) {
		this.status = orderStatus;
	}

	public void cancelOrder() {
		// 주문, 배달 정보 업데이트
		updateStatus(OrderStatus.CANCELLED);
		DeliveryStatus deliveryStatus = getDelivery().getStatus();

		if (!deliveryStatus.equals(DeliveryStatus.READY_FOR_DELIVERY)) {
			throw new DeliveryAlreadyStartedException();
		}

		getDelivery().updateStatus(DeliveryStatus.CANCELLED);

		// 재고 수량 복구
		for (var orderItem : getOrderItems()) {
			orderItem.getItem().addStockQuantity(orderItem.getItem().getStockQuantity());
		}
	}

	public long getTotalPrice() {
		long totalPrice = 0;
		for (OrderItem orderItem : getOrderItems()) {
			totalPrice += orderItem.getPriceOfItems();
		}

		if (voucher != null) {
			if (voucher.getMinLimit() > totalPrice) {
				throw new VoucherCannotUseException(
					MessageFormat.format("바우처를 사용할 수 없습니다. minLimit : {0}", voucher.getMinLimit()));
			}
			totalPrice -= voucher.getDiscountPrice(totalPrice);
		}

		return totalPrice;
	}

}
