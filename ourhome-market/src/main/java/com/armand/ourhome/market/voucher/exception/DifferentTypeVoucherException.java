package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;

public class DifferentTypeVoucherException extends BusinessException {

  public DifferentTypeVoucherException(String message) {
    super(message, ErrorCode.INVALID_TYPE_VALUE);
  }

}
