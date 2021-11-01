package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class VoucherNotFoundException extends EntityNotFoundException {

  public VoucherNotFoundException(String message) {
    super(message);
  }

}
