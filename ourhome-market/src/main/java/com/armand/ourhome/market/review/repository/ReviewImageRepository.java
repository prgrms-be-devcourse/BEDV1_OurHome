package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.market.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query(value = "select count(*) > 0 from review_image r " +
            "where r.user_id = :userId and r.status = 'ACTIVE' " +
            "limit 1", nativeQuery = true)
    boolean existsActiveImageByUserId(@Param("userId") Long userId);

    @Query("select r from ReviewImage r where r.reviewId = :reviewId and r.status = 'ACTIVE'")
    Optional<ReviewImage> findActiveImageByReviewId(@Param("reviewId") Long reviewId);

    @Query("select r from ReviewImage r where r.id = :id and r.userId = :userId and r.status = 'ACTIVE'")
    Optional<ReviewImage> findActiveImageByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

}
