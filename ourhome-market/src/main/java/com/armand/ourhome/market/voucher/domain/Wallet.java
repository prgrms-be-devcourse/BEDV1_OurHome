package com.armand.ourhome.market.voucher.domain;

import com.armand.ourhome.domain.base.BaseEntity;
import com.armand.ourhome.domain.user.User;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "wallet")
public class Wallet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voucher_id", nullable = false)
	private Voucher voucher;

	@Builder
	public Wallet(User user, Voucher voucher) {
		this.user = user;
		this.voucher = voucher;
	}

	public static Wallet of(User user, Voucher voucher) {
		return new Wallet(user, voucher);
	}

}
