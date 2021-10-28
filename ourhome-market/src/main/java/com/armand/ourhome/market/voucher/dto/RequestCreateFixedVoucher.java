package com.armand.ourhome.market.voucher.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RequestCreateFixedVoucher {

  @NotNull
  @Positive
  private Integer amount;

  @NotNull
  @Positive
  private Integer minLimit;

  @Builder
  public RequestCreateFixedVoucher(Integer amount, Integer minLimit) {
    this.amount = amount;
    this.minLimit = minLimit;
  }

}
