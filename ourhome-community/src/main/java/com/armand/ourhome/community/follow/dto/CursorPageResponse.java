package com.armand.ourhome.community.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CursorPageResponse<T> {
    private T content;
    private long totalLeftPages;
    private Long lastId;
}
