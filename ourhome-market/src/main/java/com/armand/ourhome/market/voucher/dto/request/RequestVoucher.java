package com.armand.ourhome.market.voucher.dto.request;

import com.armand.ourhome.market.voucher.dto.VoucherType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RequestVoucher {

  @NotNull
  @Positive
  private Integer value;

  @NotNull
  @Positive
  private Integer minLimit;

  private VoucherType voucherType;

  @Builder
  public RequestVoucher(Integer value, Integer minLimit, VoucherType voucherType) {
    this.value = value;
    this.minLimit = minLimit;
    this.voucherType = voucherType;
  }

}
