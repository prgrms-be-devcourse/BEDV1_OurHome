package com.armand.ourhome.market.review.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class ReviewNotFoundException extends EntityNotFoundException {

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
