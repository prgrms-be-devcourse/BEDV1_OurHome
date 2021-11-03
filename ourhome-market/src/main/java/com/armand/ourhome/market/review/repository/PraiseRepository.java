package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.market.review.domain.Praise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PraiseRepository extends JpaRepository<Praise, Long> {

    long countByReviewId(Long reviewId);

    void deletePraiseByUserIdAndReviewId(Long userId, Long reviewId);

    boolean existsByUserIdAndReviewId(Long userId, Long reviewId);

    Optional<Praise> findByIdAndUserIdAndReviewId(Long id, Long userId, Long reviewId);
}
