package com.armand.ourhome.market.voucher.dto.request;

import com.armand.ourhome.market.voucher.dto.VoucherType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequestVoucher {

	@NotNull(message = "바우처 할인 금액 또는 비율을 입력해주세요")
	@Positive(message = "바우처 할인 금액 또는 비율은 0 이상입니다")
	private Integer value;

	@NotNull(message = "바우처 사용 최소금액을 입력해주세요")
	@Positive(message = "바우처 사용 최소금액은 0원 이상입니다")
	private Integer minLimit;

	@NotNull(message = "바우처 타입을 지정해주세요")
	private VoucherType voucherType;

	@Builder
	public RequestVoucher(Integer value, Integer minLimit, VoucherType voucherType) {
		this.value = value;
		this.minLimit = minLimit;
		this.voucherType = voucherType;
	}

}
