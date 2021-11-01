package com.armand.ourhome.market.review.service.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseReview {

    private Long reviewId;
    private Long userId;
    private int rating;
    private String comment;
    private int help;
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
