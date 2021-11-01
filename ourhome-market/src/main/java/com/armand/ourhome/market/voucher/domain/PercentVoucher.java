package com.armand.ourhome.market.voucher.domain;

import java.text.MessageFormat;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("PERCENT")
@Getter
@Entity
public class PercentVoucher extends Voucher {

  @Column
  private Integer percent;

  private PercentVoucher(Integer percent, Integer minLimit) {
    super(minLimit);
    this.percent = percent;
  }

  public static PercentVoucher of(Integer percent, Integer minLimit) {
    validatePercent(percent);
    validateMinLimit(minLimit);

    return new PercentVoucher(percent, minLimit);
  }

  @Override
  public void update(int percent, int minLimit) {
    validatePercent(percent);
    validateMinLimit(minLimit);

    this.percent = percent;
    super.updateMinLimit(minLimit);
  }

  @Override
  public int getDiscountPrice(int currentPrice) {
    return currentPrice * (percent / 100);
  }

  //== Validation Method ==//
  public static void validateMinLimit(Integer minLimit) {
    Assert.notNull(minLimit, "바우처 사용 최소금액은 null이 될 수 없습니다.");

    if (minLimit <= 0) {
      throw new IllegalArgumentException(
          MessageFormat.format("바우처 사용 최소금액은 0원이상입니다. minLimit = {0}", minLimit));
    }
  }

  private static void validatePercent(Integer percent) {
    Assert.notNull(percent, "바우처 할인비율은 null이 될 수 없습니다.");

    if (percent <= 0 || percent > 100) {
      throw new IllegalArgumentException(
          MessageFormat.format("바우처 할인비율은 1~100입니다. percent = {0}", percent));
    }
  }

}