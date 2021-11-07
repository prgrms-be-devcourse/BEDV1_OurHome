package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.InvalidValueException;

public class VoucherCannotUseException extends InvalidValueException {

	public VoucherCannotUseException(String message) {
		super(message);
	}

}
