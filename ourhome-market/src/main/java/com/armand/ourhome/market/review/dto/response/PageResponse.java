package com.armand.ourhome.market.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PageResponse<T> {

    @JsonProperty("total_elements")
    private long totalElements;
    @JsonProperty("total_pages")
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
