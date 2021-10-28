package com.armand.ourhome.market.voucher.domain;

import java.text.MessageFormat;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("FIXED")
@Getter
@Entity
public class FixedVoucher extends Voucher {

  @Column(nullable = false)
  private Integer amount;

  private FixedVoucher(Integer amount, Integer minLimit) {
    super(minLimit);
    this.amount = amount;
  }

  public static FixedVoucher of(Integer amount, Integer minLimit) {
    validateAmount(amount);
    validateMinLimit(minLimit, amount);

    return new FixedVoucher(amount, minLimit);
  }

  @Override
  public int getDiscountPrice(int currentPrice) {
    return this.amount;
  }

  //== Validation Method ==//
  private static void validateMinLimit(Integer minLimit, Integer amount) {
    if (minLimit == null || minLimit < amount) {
      throw new IllegalArgumentException(
          MessageFormat.format("바우처 사용 최소 금액은 바우처 할인액보다 커야합니다. minLimit = {0}", minLimit));
    }
  }

  private static void validateAmount(Integer amount) {
    if (amount == null || amount <= 0) {
      throw new IllegalArgumentException(
          MessageFormat.format("바우처 할인액은 0원보다 커야 합니다. amount = {0}", amount));
    }
  }

}
