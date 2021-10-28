package com.armand.ourhome.market.voucher.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class VoucherDto {

  private Integer value;
  private Integer minLimit;
  private VoucherType voucherType;
  private LocalDateTime createdAt;

  @Builder
  public VoucherDto(Integer value, Integer minLimit, VoucherType voucherType,
      LocalDateTime createdAt) {
    this.value = value;
    this.minLimit = minLimit;
    this.voucherType = voucherType;
    this.createdAt = createdAt;
  }

}
