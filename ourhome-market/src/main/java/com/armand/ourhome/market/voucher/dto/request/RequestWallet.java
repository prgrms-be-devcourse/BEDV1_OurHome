package com.armand.ourhome.market.voucher.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequestWallet {

	@NotNull(message = "사용자를 할당해주세요")
	private Long userId;

	@NotNull(message = "바우처를 지정해주세요")
	private Long voucherId;

	@Builder
	public RequestWallet(Long userId, Long voucherId) {
		this.userId = userId;
		this.voucherId = voucherId;
	}

}
