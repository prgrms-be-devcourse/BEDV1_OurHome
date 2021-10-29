package com.armand.ourhome.market.review.service.dto.request;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.review.domain.Review;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

}
