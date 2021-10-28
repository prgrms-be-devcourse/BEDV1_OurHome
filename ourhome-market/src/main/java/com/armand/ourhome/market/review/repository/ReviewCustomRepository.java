package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.review.domain.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewCustomRepository {

    List<Review> findAllByUser(Pageable pageable, User user);
    List<Review> findAllByUserOrderByHelpDesc(Pageable pageable, User user);
    List<Review> findAllByItem(Pageable pageable, Item item);
    List<Review> findAllByItemOrderByHelpDesc(Pageable pageable, Item item);
    boolean existsByUserAndAndItem(User user, Item item);
}
