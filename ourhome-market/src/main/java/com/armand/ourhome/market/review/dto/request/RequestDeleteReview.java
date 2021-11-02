package com.armand.ourhome.market.review.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RequestDeleteReview {

    private Long userId;

    @Builder
    public RequestDeleteReview(Long userId) {
        this.userId = userId;
    }
}
