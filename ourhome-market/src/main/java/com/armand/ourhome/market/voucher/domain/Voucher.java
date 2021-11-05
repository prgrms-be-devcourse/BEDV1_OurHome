package com.armand.ourhome.market.voucher.domain;

import com.armand.ourhome.domain.base.BaseEntity;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "DTYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Entity
@Table(name = "voucher"
	, uniqueConstraints = {
	@UniqueConstraint(columnNames = {"amount", "percent", "min_limit"})}
)
public abstract class Voucher extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "min_limit", nullable = false)
	private Integer minLimit;

	public Voucher(Integer minLimit) {
		this.minLimit = minLimit;
	}

	public abstract void update(int value, int minLimit);

	public void updateMinLimit(int minLimit) {
		this.minLimit = minLimit;
	}

	public abstract long getDiscountPrice(long currentPrice);

}
