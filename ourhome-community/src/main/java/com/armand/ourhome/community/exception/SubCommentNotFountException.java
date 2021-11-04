package com.armand.ourhome.community.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import java.text.MessageFormat;

public class SubCommentNotFountException extends EntityNotFoundException {

    public SubCommentNotFountException(Long id) {
        super(MessageFormat.format("[{0}] 존재하지 않는 답글입니다.", id));
    }
}
