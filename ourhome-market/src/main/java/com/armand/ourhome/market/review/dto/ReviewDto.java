package com.armand.ourhome.market.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDto {

    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("itemId")
    private Long itemId;
    private int rating;
    private String comment;
    private int help;

    @Builder
    public ReviewDto(Long id, Long userId, Long itemId, int rating, String comment, int help) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
        this.help = help;
    }
}
