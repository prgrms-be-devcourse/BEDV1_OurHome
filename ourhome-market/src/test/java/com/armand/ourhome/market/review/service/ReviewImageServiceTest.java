package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.utils.AwsS3Uploader;
import com.armand.ourhome.market.review.domain.ReviewImage;
import com.armand.ourhome.market.review.domain.ReviewImageStatus;
import com.armand.ourhome.market.review.dto.response.ResponseReviewImage;
import com.armand.ourhome.market.review.exception.ReviewImageDuplicateException;
import com.armand.ourhome.market.review.exception.ReviewImageNotFoundException;
import com.armand.ourhome.market.review.repository.ReviewImageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewImageServiceTest {

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private AwsS3Uploader awsS3Uploader;

    @InjectMocks
    private ReviewImageService reviewImageService;

    @Test
    @DisplayName("리뷰 이미지가 저장된다.")
    public void testSaveReviewImage() throws Exception {
        //given
        Long reviewId = 1L;
        Long userId = 1L;
        String imageBase64 = "Image_base_64";
        String imageUrl = "image_url";

        given(reviewImageRepository.existsActiveImageByUserId(any())).willReturn(false);
        given(awsS3Uploader.upload(any(), any())).willReturn(imageUrl);

        //when
        ResponseReviewImage response = reviewImageService.saveReviewImage(reviewId, userId, imageBase64);

        //then
        assertThat(response.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("이미 저장된 이미지가 있으면 새로운 이미지는 저장되지 않는다.")
    public void testSaveReviewImageAlreadyExist() throws Exception {
        //given
        Long reviewId = 1L;
        Long userId = 1L;
        String imageBase64 = "Image_base_64";
        String imageUrl = "image_url";

        given(reviewImageRepository.existsActiveImageByUserId(any())).willReturn(true);

        //then
        Assertions.assertThrows(ReviewImageDuplicateException.class, () -> {
            //when
            ResponseReviewImage response = reviewImageService.saveReviewImage(reviewId, userId, imageBase64);
        });
    }

    @Test
    @DisplayName("저장할 이미지가 없으면 null이 반환된다.")
    public void testSaveReviewImageNull() throws Exception {
        //given
        Long reviewId = 1L;
        Long userId = 1L;
        String imageBase64 = null;

        //when
        ResponseReviewImage response = reviewImageService.saveReviewImage(reviewId, userId, imageBase64);

        //then
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("리뷰 이미지가 업데이트된다")
    public void testUpdateReviewImage() throws Exception {
        //given

        ReviewImage reviewImage = ReviewImage.of(1L, 1L, "image_url");
        String imageBase64 = "update_Image_base_64";
        String imageUrl = "update_image_url";

        given(reviewImageRepository.findActiveImageByIdAndUserId(any(), any())).willReturn(Optional.of(reviewImage));
        given(awsS3Uploader.upload(any(), any())).willReturn(imageUrl);

        //when
        ResponseReviewImage response = reviewImageService.updateReviewImage(1L, reviewImage.getUserId(), imageBase64);

        //then
        assertThat(response.getImageUrl()).isEqualTo(imageUrl);
        assertThat(reviewImage.getUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("기존 리뷰 이미지가 존재하지 않으면 업데이트 되지 않는다")
    public void testUpdateReviewImageNotExist() throws Exception {
        //given
        ReviewImage reviewImage = ReviewImage.of(1L, 1L, "image_url");
        String imageBase64 = "update_Image_base_64";
        String imageUrl = "update_image_url";

        given(reviewImageRepository.findActiveImageByIdAndUserId(any(), any())).willReturn(Optional.empty());

        //then
        Assertions.assertThrows(ReviewImageNotFoundException.class, () -> {
            //when
            ResponseReviewImage response = reviewImageService.updateReviewImage(1L, reviewImage.getUserId(), imageBase64);
        });
    }

    @Test
    @DisplayName("리뷰 이미지가 삭제된다.")
    public void testDeleteReviewImage() throws Exception {
        //given
        ReviewImage reviewImage = ReviewImage.of(2L, 2L, "imageUrl");

        given(reviewImageRepository.findActiveImageByReviewId(any())).willReturn(Optional.of(reviewImage));

        //when
        reviewImageService.deleteReviewImage(1L);

        //then
        assertThat(reviewImage.getStatus()).isEqualTo(ReviewImageStatus.INACTIVE);
    }


}