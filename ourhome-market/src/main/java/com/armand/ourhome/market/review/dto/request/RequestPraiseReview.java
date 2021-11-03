package com.armand.ourhome.market.review.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPraiseReview {

    @NotNull
    private Long userId;

    public RequestPraiseReview(Long userId) {
        this.userId = userId;
    }
}
