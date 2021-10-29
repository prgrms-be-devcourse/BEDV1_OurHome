package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;

public class DuplicateVoucherException extends BusinessException {

  public DuplicateVoucherException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

}
