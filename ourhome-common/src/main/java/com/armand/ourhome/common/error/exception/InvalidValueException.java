package com.armand.ourhome.common.error.exception;

public class InvalidValueException extends BusinessException{
    public InvalidValueException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}
