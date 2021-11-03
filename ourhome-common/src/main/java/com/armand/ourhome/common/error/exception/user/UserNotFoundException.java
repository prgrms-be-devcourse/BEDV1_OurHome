package com.armand.ourhome.common.error.exception.user;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

import java.text.MessageFormat;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long userId){
        super(MessageFormat.format("해당 사용자를 찾을 수 없습니다. (id : {})", userId));
    }
}
