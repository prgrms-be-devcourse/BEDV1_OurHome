package com.armand.ourhome.market.review.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class ReviewImageNotFoundException extends EntityNotFoundException {

    public ReviewImageNotFoundException(String message) {
        super(message);
    }
}
