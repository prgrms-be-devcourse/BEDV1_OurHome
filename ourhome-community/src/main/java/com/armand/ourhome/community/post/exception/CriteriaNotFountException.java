package com.armand.ourhome.community.post.exception;

import com.armand.ourhome.common.error.exception.InvalidValueException;

import java.text.MessageFormat;

/**
 * Created by yunyun on 2021/11/04.
 */
public class CriteriaNotFountException extends InvalidValueException {

    public CriteriaNotFountException(String typeName) {
        super(MessageFormat.format("{0}는 잘못된 검색 기준입니다.", typeName));
    }
}
