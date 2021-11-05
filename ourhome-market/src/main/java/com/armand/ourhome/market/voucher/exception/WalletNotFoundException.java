package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class WalletNotFoundException extends EntityNotFoundException {

	public WalletNotFoundException(String message) {
		super(message);
	}

}
