package com.armand.ourhome.market.voucher.exception;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.ErrorCode;

public class DuplicateWalletException extends BusinessException {

  public DuplicateWalletException(String message) {
    super(message, ErrorCode.INVALID_INPUT_VALUE);
  }
}