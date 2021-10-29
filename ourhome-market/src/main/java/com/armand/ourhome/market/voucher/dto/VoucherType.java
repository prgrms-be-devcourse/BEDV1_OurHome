package com.armand.ourhome.market.voucher.dto;

import com.armand.ourhome.market.voucher.domain.FixedVoucher;
import com.armand.ourhome.market.voucher.domain.Voucher;

public enum VoucherType { // 214page
  FIXED {
    public FixedVoucher of(int value, int minLimit) {
      return FixedVoucher.of(value, minLimit);
    }
  },
  PERCENT {
    public FixedVoucher of(int value, int minLimit) {
      return FixedVoucher.of(value, minLimit);
    }
  };

  public abstract <T extends Voucher> T of(int value, int minLimit);

}
