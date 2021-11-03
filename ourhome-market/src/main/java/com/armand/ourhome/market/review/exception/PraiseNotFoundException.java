package com.armand.ourhome.market.review.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class PraiseNotFoundException extends EntityNotFoundException {

    public PraiseNotFoundException(String message) {
        super(message);
    }
}
