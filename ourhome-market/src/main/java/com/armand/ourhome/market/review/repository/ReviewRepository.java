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
            "where r.item = :item and r.status = 'POSTED'")
    Aggregate countReviewAndAverageRatingOf(@Param("item") Item item);

    @Query("select r from Review r where r.item.id = :itemId and r.status = 'POSTED'")
    Page<Review> findByItemId(@Param("itemId") Long itemId, Pageable pageable);

    @Query("select r from Review r where r.item.id = :itemId and r.rating = :rating and r.status = 'POSTED'")
    Page<Review> findByItemIdAndRatingOrderById(@Param("itemId") Long itemId, @Param("rating") Rating rating, Pageable pageable);

    @Query(value = "select count(*) > 0 from Review where item_id = :itemId and user_id = :userId and status = 'POSTED' limit 1", nativeQuery = true)
    boolean existsByItemIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId);

}
