package com.armand.ourhome.market.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseReview {

    @JsonProperty("review_id")
    private Long reviewId;
    @JsonProperty("user_id")
    private Long userId;
    private int rating;
    private String comment;
    private int help;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Builder
    public ResponseReview(Long reviewId, Long userId, int rating, String comment, int help, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.help = help;
        this.createdAt = createdAt;
    }
}
