package com.armand.ourhome.community.post.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

/**
 * Created by yunyun on 2021/11/04.
 */
public class UserNotFountException extends EntityNotFoundException {
    public UserNotFountException(String message) {
        super(message);
    }
}