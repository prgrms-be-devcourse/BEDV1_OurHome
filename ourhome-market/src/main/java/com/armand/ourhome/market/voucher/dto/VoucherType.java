package com.armand.ourhome.market.voucher.dto;

import com.armand.ourhome.market.voucher.domain.FixedVoucher;
import com.armand.ourhome.market.voucher.domain.PercentVoucher;
import com.armand.ourhome.market.voucher.domain.Voucher;
import com.armand.ourhome.market.voucher.exception.DifferentTypeVoucherException;

public enum VoucherType {
  FIXED {
    public FixedVoucher of(int value, int minLimit) {
      return FixedVoucher.of(value, minLimit);
    }
    public void validateVoucherType(Voucher voucher){
      if(FixedVoucher.class != voucher.getClass()){
        throw new DifferentTypeVoucherException("바우처의 타입이 동일하지 않습니다");
      }
    }
  },
  PERCENT {
    public PercentVoucher of(int value, int minLimit) {
      return PercentVoucher.of(value, minLimit);
    }
    public void validateVoucherType(Voucher voucher){
      if(PercentVoucher.class != voucher.getClass()){
        throw new DifferentTypeVoucherException("바우처의 타입이 동일하지 않습니다");
      }
    }
  };

  public abstract <T extends Voucher> T of(int value, int minLimit);

  public abstract void validateVoucherType(Voucher voucher);

}
