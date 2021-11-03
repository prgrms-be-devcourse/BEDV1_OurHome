package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.market.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    boolean existsByUserId(Long userId);
}
