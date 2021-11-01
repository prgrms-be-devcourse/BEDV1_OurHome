package com.armand.ourhome.market.review.exception;

import com.armand.ourhome.common.error.exception.InvalidValueException;

public class UserAccessDeniedException extends InvalidValueException {
    public UserAccessDeniedException(String message) {
        super(message);
    }
}
