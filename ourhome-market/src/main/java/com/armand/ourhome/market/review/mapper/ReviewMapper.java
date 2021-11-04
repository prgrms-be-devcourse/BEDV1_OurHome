package com.armand.ourhome.market.review.mapper;

import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.dto.ReviewDto;
import com.armand.ourhome.market.review.dto.request.RequestAddReview;
import com.armand.ourhome.market.review.dto.response.ResponseReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewMapper {

    @Mapping(target = "reviewId", source = "review.id")
    @Mapping(target = "praise", source = "isPraise")
    @Mapping(target = "userId", expression = "java(review.getUser().getId())")
    ResponseReview toResponseDto(Review review, boolean isPraise);

    default ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .comment(review.getComment())
                .help(review.getHelp())
                .itemId(review.getItem().getId())
                .rating(review.getRating())
                .userId(review.getUser().getId())
                .build();
    }
}
