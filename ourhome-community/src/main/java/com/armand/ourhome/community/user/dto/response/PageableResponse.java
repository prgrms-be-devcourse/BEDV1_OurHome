package com.armand.ourhome.community.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageableResponse<T> {
    private int number;         // 현재 몇번째 페이지인지
    private int totalPages;     // 총 페이지 개수
    private T content;          // 콘텐츠 (리스트)

    public PageableResponse(int number, int totalPages, T content) {
        this.number = number;
        this.totalPages = totalPages;
        this.content = content;
    }
}
