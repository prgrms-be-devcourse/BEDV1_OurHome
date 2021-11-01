package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.market.review.domain.Aggregate;
import com.armand.ourhome.market.review.domain.Rating;
import com.armand.ourhome.market.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.armand.ourhome.market.review.domain.Aggregate(count(r), avg(r.rating)) " +
            "from Review r " +
            "where r.item = :item")
    Aggregate countReviewAndAverageRatingOf(@Param("item") Item item);

    Page<Review> findByItemId(Long itemId, Pageable pageable);

    Page<Review> findByItemIdAndRatingOrderById(Long itemId, Rating rating, Pageable pageable);

    boolean existsByItemIdAndUserId(Long itemId, Long userId);
}
