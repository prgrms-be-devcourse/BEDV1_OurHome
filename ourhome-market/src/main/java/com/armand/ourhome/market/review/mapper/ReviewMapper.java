package com.armand.ourhome.market.review.mapper;

import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import com.armand.ourhome.market.review.service.dto.request.RequestAddReview;
import com.armand.ourhome.market.review.service.dto.response.ResponseReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewMapper {

    Review toEntity(RequestAddReview request);

    @Mapping(target = "reviewId", source = "id")
    ResponseReview toResponseDto(Review review);

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
