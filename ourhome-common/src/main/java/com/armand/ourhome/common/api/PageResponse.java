package com.armand.ourhome.common.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private T content;
    private int number;
    private int totalPages;
    private int numberOfElements;
    private long totalElements;
}
