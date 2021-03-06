package com.armand.ourhome.market.order.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class DeliveryNotFoundException extends EntityNotFoundException {

    public DeliveryNotFoundException(String target) {
        super("Delivery " + target + " is not found");
    }
}
