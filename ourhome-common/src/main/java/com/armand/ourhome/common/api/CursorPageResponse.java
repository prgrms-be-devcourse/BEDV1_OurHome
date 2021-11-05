package com.armand.ourhome.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CursorPageResponse<T> {
    private T content;
    private Long lastId;
}
