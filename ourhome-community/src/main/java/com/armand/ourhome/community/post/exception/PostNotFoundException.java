package com.armand.ourhome.community.post.exception;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;

import java.text.MessageFormat;

/**
 * Created by yunyun on 2021/11/04.
 */
public class PostNotFoundException extends EntityNotFoundException {

    public PostNotFoundException(String criteria, String criteriaName) {
        super(MessageFormat.format("찾고 계신 {0}는 해당 게시물에서 찾지 못하였습니다. (검색 기준: {1})", criteria, criteriaName));
    }
}
