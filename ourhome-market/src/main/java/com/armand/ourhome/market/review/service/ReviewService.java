package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.api.PageResponse;
import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.item.exception.ItemNotFoundException;
import com.armand.ourhome.market.order.exception.UserNotFoundException;
import com.armand.ourhome.market.order.repository.OrderItemRepository;
import com.armand.ourhome.market.review.domain.Aggregate;
import com.armand.ourhome.market.review.domain.Praise;
import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.dto.request.*;
import com.armand.ourhome.market.review.dto.response.*;
import com.armand.ourhome.market.review.exception.PraiseDuplicationException;
import com.armand.ourhome.market.review.exception.PraiseNotFoundException;
import com.armand.ourhome.market.review.exception.ReviewNotFoundException;
import com.armand.ourhome.market.review.exception.UserAccessDeniedException;
import com.armand.ourhome.market.review.mapper.ReviewMapper;
import com.armand.ourhome.market.review.repository.PraiseRepository;
import com.armand.ourhome.market.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewImageService reviewImageService;

    private final ReviewMapper reviewMapper = Mappers.getMapper(ReviewMapper.class);
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final PraiseRepository praiseRepository;


    @Transactional
    public ResponseAddReview save(RequestAddReview request) {
        Review review = createReview(request);
        reviewRepository.save(review);

        ResponseReviewImage responseReviewImage = saveReviewImage(request, review);

        return new ResponseAddReview(review.getId(), responseReviewImage);
    }

    private ResponseReviewImage saveReviewImage(RequestAddReview request, Review review) {
        return reviewImageService.saveReviewImage(review.getId(), request.getUserId(), request.getReviewImageBase64());
    }

    @Transactional
    public ResponseUpdateReview update(Long reviewId, RequestUpdateReview request) {
        Review review = getReview(reviewId);

        if (!review.isWrittenBy(request.getUserId())) {
            throw new UserAccessDeniedException(MessageFormat.format("?????? ???????????? ????????????. userId = {0}", request.getUserId()));
        }

        review.update(request.getComment(), request.getRating());

        ResponseReviewImage responseReviewImage = updateReviewImage(request, review);

        return new ResponseUpdateReview(reviewId, responseReviewImage);
    }

    private ResponseReviewImage updateReviewImage(RequestUpdateReview request, Review review) {

        return reviewImageService.updateReviewImage(review.getId(), request.getUserId(), request.getReviewImageBase64());
    }

    @Transactional
    public void delete(Long reviewId, RequestDeleteReview request) {
        Review review = getReview(reviewId);

        if (!review.isWrittenBy(request.getUserId())) {
            throw new UserAccessDeniedException(MessageFormat.format("?????? ???????????? ????????????. userId = {0}", request.getUserId()));
        }

        review.delete();

        deleteReviewImage(reviewId);
    }

    private void deleteReviewImage(Long reviewId) {
        reviewImageService.deleteReviewImage(reviewId);
    }

    @Transactional
    public Long praiseReview(Long reviewId, RequestPraiseReview request) {

        Review review = getReview(reviewId);

        if (review.isWrittenBy(request.getUserId())) {
            throw new UserAccessDeniedException(MessageFormat.format("?????? ???????????? '????????? ??????' ??? ????????? ??? ????????????. reviewId = {0}, userId = {1}", reviewId, request.getUserId()));
        }

        review.addHelp();

        Praise praise = createPraise(reviewId, request.getUserId());
        praiseRepository.save(praise);

        return reviewId;
    }

    @Transactional
    public void deletePraise(Long praiseId, Long reviewId, RequestRemovePraiseReview request) {

        Praise praise = praiseRepository.findByIdAndUserIdAndReviewId(praiseId, request.getUserId(), reviewId)
                .orElseThrow(() -> new PraiseNotFoundException(
                        MessageFormat.format("'????????? ??????' ??? ???????????? ????????????. praiseId = {0}, reviewId = {1}, userId = {2}",
                        praiseId, reviewId, request.getUserId())));

        Review review = getReview(reviewId);
        review.removeHelp();

        praiseRepository.delete(praise);
    }

    @Transactional
    public void deleteReviewImageBy(Long reviewId) {
        reviewImageService.deleteReviewImage(reviewId);
    }

    public PageResponse<List<ResponseReview>> fetchReviewPagesBy(Long itemId, RequestReviewPages request) {
        Page<Review> pages = reviewRepository.findByItemId(itemId, request.of());

        List<ResponseReview> reviews = pages
                .getContent()
                .stream()
                .map(review -> reviewMapper.toResponseDto(review, checkReviewPraised(request.getUserId(), review.getId())))
                .collect(Collectors.toList());

        return PageResponse.<List<ResponseReview>>builder()
                .number(pages.getNumber())
                .numberOfElements(pages.getNumberOfElements())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .content(reviews)
                .build();
    }

    public boolean checkReviewPraised(Long userId, Long reviewId) {
        return praiseRepository.existsByUserIdAndReviewId(userId, reviewId);
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
                .orElseThrow(() -> new ReviewNotFoundException(MessageFormat.format("?????? ????????? ???????????? ????????????. reviewId = {0}", reviewId)));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(MessageFormat.format("?????? ????????? ???????????? ????????????. itemId = {0}", itemId)));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat.format("?????? ???????????? ???????????? ????????????. userId = {0}", userId)));
    }

    private void validUserOrderedItem(Long itemId, Long userId) {
        boolean exists = orderItemRepository.existsOrderItemByUserIdAndItemId(itemId, userId);

        if (!exists) {
            throw new UserAccessDeniedException(MessageFormat.format("????????? ???????????? ?????? ?????? ????????? ?????? ??? ????????????. itemId = {0}, userId = {1}", itemId, userId));
        }
    }

    private void validReviewAlreadyExists(Long itemId, Long userId) {
        boolean exists = reviewRepository.existsByItemIdAndUserId(itemId, userId);

        if (exists) {
            throw new InvalidValueException(
                    MessageFormat.format("???????????? ?????? ????????? ???????????????. itemId = {0}, userId = {1}", itemId, userId));
        }
    }

    private Praise createPraise(Long reviewId, Long userId) {

        boolean exists = praiseRepository.existsByUserIdAndReviewId(userId, reviewId);

        if(exists) {
            throw new PraiseDuplicationException(MessageFormat.format("'????????? ??????' ??? ?????? ?????????????????????. reviewId = {0}, userId = {1}", reviewId, userId));
        }

        return new Praise(userId, reviewId);
    }
}
