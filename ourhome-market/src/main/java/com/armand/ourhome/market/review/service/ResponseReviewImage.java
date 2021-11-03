package com.armand.ourhome.market.review.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseReviewImage {

    private Long id;
    private String imageUrl;

    public ResponseReviewImage(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
