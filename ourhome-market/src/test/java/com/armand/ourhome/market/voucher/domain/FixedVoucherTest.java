package com.armand.ourhome.market.voucher.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FixedVoucherTest {

  @Test
  @DisplayName("할인금액은 null이 될 수 없다")
  void cannot_setPercent_forNull() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      FixedVoucher voucher = FixedVoucher.of(null, 1000);
    });
  }

  @Test
  @DisplayName("할인금액은 0보다 작을 수 없다")
  void cannot_setPercent_Below0() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      FixedVoucher voucher = FixedVoucher.of(0, 1000);
    });
  }

  @Test
  @DisplayName("바우처 사용 최소액은 null이 될 수 없다")
  void cannot_setMinLimit_forNull() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      FixedVoucher voucher = FixedVoucher.of(3000, null);
    });
  }

  @Test
  @DisplayName("바우처 사용 최소금액은 할인금액보다 작을 수 없다")
  void cannot_setMinLimit_LessThanAmount() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      FixedVoucher voucher = FixedVoucher.of(3000, 2000);
    });
  }

  @Test
  @DisplayName("바우처를 정상적으로 생성할 수 있다")
  void testSaveVoucher() {
    // when
    FixedVoucher voucher = FixedVoucher.of(3000, 10000);

    // then
    assertThat(voucher).isNotNull();
  }

}
