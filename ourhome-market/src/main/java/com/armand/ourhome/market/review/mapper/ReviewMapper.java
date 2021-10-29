package com.armand.ourhome.market.review.mapper;

import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import com.armand.ourhome.market.review.service.dto.request.RequestAddReview;
import org.mapstruct.Mapper;

@Mapper
public interface ReviewMapper {

    Review toEntity(RequestAddReview request);

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
