package com.armand.ourhome.market.review.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestAddReview {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @JsonProperty("item_id")
    private Long itemId;

    @NotBlank
    private String comment;

    @Min(0)
    @Max(5)
    private int rating;

    @Builder
    public RequestAddReview(Long userId, Long itemId, String comment, int rating) {
        this.userId = userId;
        this.itemId = itemId;
        this.comment = comment;
        this.rating = rating;
    }
}
