package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.market.review.domain.ReviewImage;
import com.armand.ourhome.market.review.dto.response.ResponseReviewImage;
import com.armand.ourhome.market.review.exception.ReviewImageDuplicateException;
import com.armand.ourhome.market.review.exception.ReviewImageNotFoundException;
import com.armand.ourhome.market.review.exception.UserAccessDeniedException;
import com.armand.ourhome.market.review.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public ResponseReviewImage saveReviewImage(Long reviewId, Long userId, String reviewImageBase64) {

        if (Objects.isNull(reviewImageBase64))
            return null;

        if (reviewImageRepository.existsActiveImageByUserId(userId)) {
            throw new ReviewImageDuplicateException(MessageFormat.format("리뷰에 등록한 이미지가 이미 존재합니다. userId = {0}", userId));
        }

        String reviewImageUrl = awsS3Uploader.upload(reviewImageBase64, "review");
        ReviewImage reviewImage = ReviewImage.of(reviewId, userId, reviewImageUrl);

        reviewImageRepository.save(reviewImage);

        return new ResponseReviewImage(reviewImage.getId(), reviewImageUrl);
    }

    @Transactional
    public ResponseReviewImage updateReviewImage(Long id, Long userId, String reviewImageBase64) {

        if (Objects.isNull(reviewImageBase64))
            return null;

        ReviewImage reviewImage = reviewImageRepository.findActiveImageByIdAndUserId(id, userId)
                .orElseThrow(() -> new ReviewImageNotFoundException(MessageFormat.format("리뷰 이미지가 존재 하지 않습니다. id = {0}, userId = {0}", id, userId)));

        String reviewImageUrl = awsS3Uploader.upload(reviewImageBase64, "review");

        reviewImage.update(reviewImageUrl);

        return new ResponseReviewImage(id, reviewImageUrl);
    }

    @Transactional
    public void deleteReviewImage(Long reviewId) {
        ReviewImage reviewImage = reviewImageRepository.findActiveImageByReviewId(reviewId)
                .orElseThrow(() -> new ReviewImageNotFoundException(MessageFormat.format("리뷰 이미지가 존재 하지 않습니다. reviewId = {0}", reviewId)));

        reviewImage.delete();
    }
}
