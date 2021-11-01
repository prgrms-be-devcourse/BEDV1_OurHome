package com.armand.ourhome.market.order.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {

    public ItemNotFoundException(String target) {
        super("Item " + target + " is not found");
    }
}
