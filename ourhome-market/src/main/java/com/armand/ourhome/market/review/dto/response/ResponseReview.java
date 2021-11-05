package com.armand.ourhome.market.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseReview {

    private Long reviewId;
    private Long userId;
    private int rating;
    private String comment;
    private int help;
    @JsonProperty("is_praise")
    private boolean praise;
    private LocalDateTime createdAt;
}
