package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;

public class DifferentTypeVoucherException extends BusinessException {

  public DifferentTypeVoucherException(String message, ErrorCode errorCode) {
    super(message, errorCode);
  }

}
