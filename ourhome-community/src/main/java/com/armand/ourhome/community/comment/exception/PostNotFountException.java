package com.armand.ourhome.community.comment.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import java.text.MessageFormat;

public class PostNotFountException extends EntityNotFoundException {

    public PostNotFountException(Long id) {
        super(MessageFormat.format("[{0}] 존재하지 않는 게시물입니다.", id));
    }
}
