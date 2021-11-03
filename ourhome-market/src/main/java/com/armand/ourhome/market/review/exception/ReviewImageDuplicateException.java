package com.armand.ourhome.market.review.exception;

import com.armand.ourhome.common.error.exception.InvalidValueException;

public class ReviewImageDuplicateException extends InvalidValueException {
    public ReviewImageDuplicateException(String message) {
        super(message);
    }
}
