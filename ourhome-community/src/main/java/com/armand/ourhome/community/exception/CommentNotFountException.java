package com.armand.ourhome.community.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import java.text.MessageFormat;

public class CommentNotFountException extends EntityNotFoundException {

    public CommentNotFountException(Long id) {
        super(MessageFormat.format("[{0}] 존재하지 않는 댓글입니다.", id));
    }
}
