package com.armand.ourhome.market.voucher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VoucherDto {

	@JsonProperty("voucher_id")
	private Long id;
	private Integer value;
	@JsonProperty("min_limit")
	private Integer minLimit;
	@JsonProperty("voucher_type")
	private VoucherType voucherType;
	@JsonProperty("created_at")
	private LocalDateTime createdAt;
	@JsonProperty("updated_at")
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
