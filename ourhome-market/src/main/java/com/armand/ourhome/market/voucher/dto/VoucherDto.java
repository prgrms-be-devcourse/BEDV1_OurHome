package com.armand.ourhome.market.voucher.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VoucherDto {

  private Long id;
  private Integer value;
  private Integer minLimit;
  private VoucherType voucherType;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Builder
  public VoucherDto(Long id, Integer value, Integer minLimit, VoucherType voucherType,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.value = value;
    this.minLimit = minLimit;
    this.voucherType = voucherType;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

}
