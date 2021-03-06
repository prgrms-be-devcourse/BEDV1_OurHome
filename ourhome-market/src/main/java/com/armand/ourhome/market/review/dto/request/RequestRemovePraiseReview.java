package com.armand.ourhome.market.review.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestRemovePraiseReview {

    @NotNull
    private Long userId;

    public RequestRemovePraiseReview(Long userId) {
        this.userId = userId;
    }
}
