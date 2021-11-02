package com.armand.ourhome.market.voucher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WalletDto {

  @JsonProperty("wallet_id")
  private Long id;
  private Long userId;
  private VoucherDto voucherDto;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Builder
  public WalletDto(Long id, Long userId, VoucherDto voucherDto, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.voucherDto = voucherDto;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
