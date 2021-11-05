package com.armand.ourhome.market.review.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUpdateReview {

    private Long reviewId;
    private ResponseReviewImage reviewImage;

    public ResponseUpdateReview(Long reviewId, ResponseReviewImage reviewImage) {
        this.reviewId = reviewId;
        this.reviewImage = reviewImage;
    }
}
