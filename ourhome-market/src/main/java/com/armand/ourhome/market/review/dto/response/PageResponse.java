package com.armand.ourhome.market.review.service.dto.response;

import com.armand.ourhome.market.review.service.dto.ReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {

    private long totalElements;
    private int totalPages;
    private T content;
    private int size;

    @Builder
    public PageResponse(long totalElements, int totalPages, T content, int size) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
        this.size = size;
    }
}
