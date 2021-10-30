package com.armand.ourhome.market.voucher.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PercentVoucherTest {

  @Test
  @DisplayName("할인율은 null이 될 수 없다")
  void cannot_setPercent_forNull() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      PercentVoucher voucher = PercentVoucher.of(null, 1000);
    });
  }

  @Test
  @DisplayName("할인율은 0이하가 될 수 없다")
  void cannot_setPercent_Below0() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      PercentVoucher voucher = PercentVoucher.of(0, 1000);
    });
  }

  @Test
  @DisplayName("바우처 사용 최소액은 null이 될 수 없다")
  void cannot_setMinLimit_forNull() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      PercentVoucher voucher = PercentVoucher.of(20, null);
    });
  }

  @Test
  @DisplayName("바우처 사용 최소금액은 0이하가 될 수 없다")
  void cannot_setMinLimit_Below0() {
    // then
    assertThrows(IllegalArgumentException.class, () -> {
      // when
      PercentVoucher voucher = PercentVoucher.of(20, 0);
    });
  }

  @Test
  @DisplayName("바우처를 정상적으로 생성할 수 있다")
  void testSaveVoucher() {
    // when
    PercentVoucher voucher = PercentVoucher.of(20, 1000);

    // then
    assertThat(voucher).isNotNull();
  }

}
