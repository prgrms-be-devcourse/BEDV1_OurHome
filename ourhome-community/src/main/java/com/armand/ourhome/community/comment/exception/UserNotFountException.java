package com.armand.ourhome.community.comment.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import java.text.MessageFormat;

public class UserNotFountException extends EntityNotFoundException {

    public UserNotFountException(Long id) {
        super(MessageFormat.format("[{0}] 존재하지 않는 사용자입니다.", id));
    }
}
