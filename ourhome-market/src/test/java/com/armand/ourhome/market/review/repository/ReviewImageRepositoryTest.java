package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.market.review.domain.ReviewImage;
import com.armand.ourhome.market.review.domain.ReviewImageStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(OurHomeDomainConfig.class)
@DataJpaTest
class ReviewImageRepositoryTest {

    @Autowired
    private ReviewImageRepository reviewImageRepository;
    
    
    @Test
    @DisplayName("ACTIVE 이미지가 조회된다.")
    public void testFindReviewImage() throws Exception {
        //given
        ReviewImage image = ReviewImage.of(2L, 2L, "image_url");

        //when 
        reviewImageRepository.save(image);

        //then
        Optional<ReviewImage> actual = reviewImageRepository.findActiveImageByReviewId(image.getReviewId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getStatus()).isEqualTo(ReviewImageStatus.ACTIVE);
        assertThat(actual.get().getUrl()).isEqualTo(image.getUrl());
    }

    @Test
    @DisplayName("INACTIVE 이미지는 조회되지 않는다")
    public void testFindInActiveReviewImage() throws Exception {
        //given
        ReviewImage image = ReviewImage.of(2L, 2L, "image_url");

        //when
        reviewImageRepository.save(image);
        image.delete();

        //then
        Optional<ReviewImage> actual = reviewImageRepository.findActiveImageByReviewId(image.getReviewId());
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("INACTIVE 이미지는 카운팅 되지 않는다")
    public void testExistsInActiveImage() throws Exception {
        //given
        ReviewImage image = ReviewImage.of(1L, 1L, "image_url");

        //when
        reviewImageRepository.save(image);
        image.delete();

        //then
        boolean exist = reviewImageRepository.existsActiveImageByUserId(image.getUserId());
        assertThat(exist).isFalse();
    }




}