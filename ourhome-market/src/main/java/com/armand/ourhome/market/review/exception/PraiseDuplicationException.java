package com.armand.ourhome.market.review.exception;

import com.armand.ourhome.common.error.exception.InvalidValueException;

public class PraiseDuplicationException extends InvalidValueException {
    public PraiseDuplicationException(String message) {
        super(message);
    }
}
