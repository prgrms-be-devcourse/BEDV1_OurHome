package com.armand.ourhome.market.order.exception;

import com.armand.ourhome.common.error.exception.BusinessException;
import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.common.error.exception.ErrorCode;

// 다른 exception 추가 예정
public class DeliveryAlreadyStartedException extends EntityNotFoundException {

    public DeliveryAlreadyStartedException() {
        super("Delivery already started");
    }
}
