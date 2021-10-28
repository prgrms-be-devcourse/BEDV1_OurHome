package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.market.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {

}
