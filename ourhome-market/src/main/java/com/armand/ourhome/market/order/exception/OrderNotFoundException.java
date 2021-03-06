package com.armand.ourhome.market.order.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {

    public OrderNotFoundException(String target) {
        super("Order " + target + " is not found");
    }
}
