package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.market.review.domain.ReviewImage;
import com.armand.ourhome.market.review.exception.ReviewImageDuplicateException;
import com.armand.ourhome.market.review.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public ResponseReviewImage saveReviewImage(Long reviewId, Long userId, String reviewImageBase64) {

        if (Objects.isNull(reviewImageBase64))
            return null;

        if (reviewImageRepository.existsByUserId(userId)) {
            throw new ReviewImageDuplicateException(MessageFormat.format("리뷰에 등록한 이미지가 이미 존재합니다. userId = {0}", userId));
        }

        String reviewImageUrl = awsS3Uploader.upload(reviewImageBase64, "review-images");
        ReviewImage reviewImage = ReviewImage.of(reviewId, userId, reviewImageUrl);

        reviewImageRepository.save(reviewImage);

        return new ResponseReviewImage(reviewImage.getId(), reviewImageUrl);
    }
}
