package com.armand.ourhome.market.review.dto.response;

import com.armand.ourhome.market.review.service.ResponseReviewImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseAddReview {

    private Long reviewId;
    private ResponseReviewImage reviewImage;

    public ResponseAddReview(Long reviewId, ResponseReviewImage reviewImage) {
        this.reviewId = reviewId;
        this.reviewImage = reviewImage;
    }
}
