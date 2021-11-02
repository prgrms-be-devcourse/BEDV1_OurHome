package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.item.exception.ItemNotFoundException;
import com.armand.ourhome.market.order.exception.UserNotFoundException;
import com.armand.ourhome.market.order.repository.OrderItemRepository;
import com.armand.ourhome.market.review.domain.Aggregate;
import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.dto.request.RequestDeleteReview;
import com.armand.ourhome.market.review.dto.request.RequestUpdateReview;
import com.armand.ourhome.market.review.exception.ReviewNotFoundException;
import com.armand.ourhome.market.review.exception.UserAccessDeniedException;
import com.armand.ourhome.market.review.mapper.ReviewMapper;
import com.armand.ourhome.market.review.repository.ReviewRepository;
import com.armand.ourhome.market.review.dto.request.RequestAddReview;
import com.armand.ourhome.market.review.dto.request.RequestReviewPages;
import com.armand.ourhome.market.review.dto.response.PageResponse;
import com.armand.ourhome.market.review.dto.response.ResponseReview;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewMapper reviewMapper = Mappers.getMapper(ReviewMapper.class);
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Long save(RequestAddReview request) {
        Review review = createReview(request);
        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional
    public Long update(Long reviewId, RequestUpdateReview request) {
        Review review = getReview(reviewId);

        review.validateUser(request.getUserId());

        review.update(request.getComment(), request.getRating());

        return reviewId;
    }

    @Transactional
    public void delete(Long reviewId, RequestDeleteReview request) {
        Review review = getReview(reviewId);

        review.validateUser(request.getUserId());

        review.delete();
    }

    public PageResponse<List<ResponseReview>> fetchReviewPagesBy(Long itemId, RequestReviewPages request) {
        Page<Review> pages = reviewRepository.findByItemId(itemId, request.of());

        List<ResponseReview> reviews = pages
                .getContent()
                .stream()
                .map(reviewMapper::toResponseDto)
                .collect(Collectors.toList());

        return new PageResponse<>(pages.getTotalElements(), pages.getTotalPages(), reviews, pages.getSize());
    }

    public Aggregate getReviewAggregateBy(Item item) {
        return reviewRepository.countReviewAndAverageRatingOf(item);
    }

    private Review createReview(RequestAddReview request) {
        validUserOrderedItem(request.getItemId(), request.getUserId());
        validReviewAlreadyExists(request.getItemId(), request.getUserId());

        User user = getUser(request.getUserId());
        Item item = getItem(request.getItemId());

        return Review.of(user, item, request.getRating(), request.getComment());
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(MessageFormat.format("해당 리뷰가 존재하지 않습니다. reviewId = {0}", reviewId)));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(MessageFormat.format("해당 상품이 존재하지 않습니다. itemId = {0}", itemId)));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat.format("해당 사용자는 존재하지 않습니다. userId = {0}", userId)));
    }

    private void validUserOrderedItem(Long itemId, Long userId) {
        boolean exists = orderItemRepository.existsOrderItemByUserIdAndItemId(itemId, userId);

        if (!exists) {
            throw new UserAccessDeniedException(MessageFormat.format("상품을 구매하지 않는 경우 리뷰를 남길 수 없습니다. itemId = {0}, userId = {1}", itemId, userId));
        }
    }

    private void validReviewAlreadyExists(Long itemId, Long userId) {
        boolean exists = reviewRepository.existsByItemIdAndUserId(itemId, userId);

        if (exists) {
            throw new InvalidValueException(
                    MessageFormat.format("사용자가 이미 리뷰를 남겼습니다. itemId = {0}, userId = {1}", itemId, userId));
        }
    }
}
