package com.armand.ourhome.market.voucher.dto;

import com.armand.ourhome.market.voucher.domain.FixedVoucher;
import com.armand.ourhome.market.voucher.domain.PercentVoucher;
import com.armand.ourhome.market.voucher.domain.Voucher;

public enum VoucherType {
  
  FIXED {
    public FixedVoucher of(int value, int minLimit) {
      return FixedVoucher.of(value, minLimit);
    }
    public boolean isDifferentType(Voucher voucher){
      return FixedVoucher.class != voucher.getClass();
    }
  },
  PERCENT {
    public PercentVoucher of(int value, int minLimit) {
      return PercentVoucher.of(value, minLimit);
    }
    public boolean isDifferentType(Voucher voucher){
      return PercentVoucher.class != voucher.getClass();
    }
  };

  public abstract <T extends Voucher> T of(int value, int minLimit);

  public abstract boolean isDifferentType(Voucher voucher);

}