package com.armand.ourhome.market.review.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
public class ResponseReviewImage {

    private Long id;
    private String imageUrl;

    public ResponseReviewImage(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
