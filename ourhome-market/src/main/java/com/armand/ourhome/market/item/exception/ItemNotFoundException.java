package com.armand.ourhome.market.item.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
