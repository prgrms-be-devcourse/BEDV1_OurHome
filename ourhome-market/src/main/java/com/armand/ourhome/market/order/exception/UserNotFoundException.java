package com.armand.ourhome.market.order.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(String target) {
        super("User " + target + " is not found");
    }
}
