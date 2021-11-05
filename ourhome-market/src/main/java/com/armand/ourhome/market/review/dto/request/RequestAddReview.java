package com.armand.ourhome.market.review.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class RequestAddReview {

    @NotNull
    private Long userId;

    @NotNull
    private Long itemId;

    @NotBlank
    private String comment;

    @Min(0)
    @Max(5)
    private int rating;

    private String reviewImageBase64;
}
