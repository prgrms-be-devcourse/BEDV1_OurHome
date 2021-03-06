package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.repository.OrderItemRepository;
import com.armand.ourhome.market.review.domain.Praise;
import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.domain.ReviewStatus;
import com.armand.ourhome.market.review.dto.request.*;
import com.armand.ourhome.market.review.exception.PraiseDuplicationException;
import com.armand.ourhome.market.review.exception.UserAccessDeniedException;
import com.armand.ourhome.market.review.repository.PraiseRepository;
import com.armand.ourhome.market.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PraiseRepository praiseRepository;

    @Mock
    private ReviewImageService reviewImageService;

    private User user = User.builder()
            .description("Hello i'm user")
            .createdAt(LocalDateTime.now())
            .address("address123@naver.com")
            .id(1L)
            .nickname("nickname!")
            .password("mypassword123!@#")
            .profileImageUrl("imageurl")
            .build();

    private Item item = Item.builder()
            .name("item")
            .stockQuantity(1000)
            .imageUrl("imageurl")
            .category(Category.DAILY_NECESSITIES)
            .company(new Company("company"))
            .price(100000)
            .description("this is item. very good")
            .build();

    @Test
    @DisplayName("????????? ????????????")
    public void testSaveReview() throws Exception {
        //given
        RequestAddReview request = RequestAddReview.builder()
                .itemId(1L)
                .comment("???????????? ?????? ???????????????. ???????????? ?????? ???????????? ????????????!")
                .rating(4)
                .userId(1L)
                .build();

        Review review = Review.of(user, item, request.getRating(), request.getComment());

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        given(orderItemRepository.existsOrderItemByUserIdAndItemId(any(), any())).willReturn(true);
        given(reviewRepository.existsByItemIdAndUserId(any(), any())).willReturn(false);
        given(reviewRepository.save(any())).willReturn(review);

        //when
        reviewService.save(request);

        //then
        assertThat(review.getRating()).isEqualTo(request.getRating());
        assertThat(review.getHelp()).isEqualTo(0);
        assertThat(review.getComment()).isEqualTo(request.getComment());
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????? ????????? ??? ??????")
    public void testSaveReviewAlreadyExist() throws Exception {
        //given
        RequestAddReview request = RequestAddReview.builder()
                .itemId(1L)
                .comment("???????????? ?????? ???????????????. ???????????? ?????? ???????????? ????????????!")
                .rating(4)
                .userId(1L)
                .build();

        Review review = Review.of(user, item, request.getRating(), request.getComment());

        given(orderItemRepository.existsOrderItemByUserIdAndItemId(any(), any())).willReturn(true);
        given(reviewRepository.existsByItemIdAndUserId(any(), any())).willReturn(true);

        //then
        assertThrows(InvalidValueException.class, () -> {
            //when
            reviewService.save(request);
        });
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ???????????? ????????? ????????? ??? ??????.")
    public void testSaveReviewNotOrderItem() throws Exception {
        //given
        RequestAddReview request = RequestAddReview.builder()
                .itemId(1L)
                .comment("???????????? ?????? ???????????????. ???????????? ?????? ???????????? ????????????!")
                .rating(4)
                .userId(1L)
                .build();

        Review review = Review.of(user, item, request.getRating(), request.getComment());

        given(orderItemRepository.existsOrderItemByUserIdAndItemId(any(), any())).willReturn(false);

        //then
        assertThrows(UserAccessDeniedException.class, () -> {
            //when
            reviewService.save(request);
        });

    }
    
    @Test
    @DisplayName("????????? ????????? ??? ??????")
    public void testUpdateReview() throws Exception {
        //given 
        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ??? ???????????? ????????????!");
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        String comment = "????????? ????????? ?????? ??????????????????. ????????? ???????????????";
        int rating = 3;

        RequestUpdateReview request = RequestUpdateReview.builder()
                .comment(comment)
                .rating(rating)
                .userId(user.getId())
                .build();
        //when 
        reviewService.update(1L, request);

        //then
        assertThat(review.getComment()).isEqualTo(comment);
        assertThat(review.getRating()).isEqualTo(rating);
    }
    
    @Test
    @DisplayName("?????? ???????????? ????????? ????????? ????????? ??? ??????")
    public void testUpdateReviewNotWriter() throws Exception {
        //given 
        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ??? ???????????? ????????????!");
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        String comment = "????????? ????????? ?????? ??????????????????. ????????? ???????????????";
        int rating = 3;

        RequestUpdateReview request = RequestUpdateReview.builder()
                .comment(comment)
                .rating(rating)
                .userId(user.getId() + 1)
                .build();
        //then
        assertThrows(UserAccessDeniedException.class, () -> {
            //when
            reviewService.update(1L, request);
        });
    }
    
    @Test 
    @DisplayName("????????? ????????? ??? ??????")
    public void testDeleteReview() throws Exception {
        //given
        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ??? ???????????? ????????????!");
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        //when 
        reviewService.delete(1L, new RequestDeleteReview(user.getId(), 1L));

        //then 
        assertThat(review.getStatus()).isEqualTo(ReviewStatus.DELETED);
    }

    @Test
    @DisplayName("?????? ???????????? ????????? ??? ??????")
    public void testDeleteReviewNotWriter() throws Exception {
        //given
        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ??? ???????????? ????????????!");

        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        //then
        assertThrows(UserAccessDeniedException.class, () -> {
            //when
            reviewService.delete(1L, new RequestDeleteReview(user.getId() + 1, null));
        });
    }

    @Test
    @DisplayName("????????? '????????? ??????'??? ????????? ??? ??????.")
    public void testPraiseReview() throws Exception {
        //given
        RequestPraiseReview request = new RequestPraiseReview(user.getId() + 1);

        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ?????? ???????????? ????????????.");
        Praise praise = new Praise(user.getId() + 1, 1L);

        given(reviewRepository.findById(any())).willReturn(Optional.of(review));
        given(praiseRepository.existsByUserIdAndReviewId(any(), any())).willReturn(false);
        given(praiseRepository.save(any())).willReturn(praise);

        //when
        reviewService.praiseReview(1L, request);

        //then
        assertThat(review.getHelp()).isEqualTo(1);
    }

    @Test
    @DisplayName("?????? ???????????? '????????? ??????'??? ????????? ??? ??????.")
    public void testPraiseReviewWriter() throws Exception {
        //given

        RequestPraiseReview request = new RequestPraiseReview(user.getId());

        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ?????? ???????????? ????????????.");
        Praise praise = new Praise(user.getId() + 1, 1L);

        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        //then
        assertThrows(UserAccessDeniedException.class, () -> {
            //when
            reviewService.praiseReview(1L, request);
        });
    }
    
    @Test
    @DisplayName("'????????? ??????'??? ?????? ????????? ??? ??????")
    public void testPraiseReviewDuplicate() throws Exception {
        //given

        RequestPraiseReview request = new RequestPraiseReview(user.getId() + 1);

        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ?????? ???????????? ????????????.");
        Praise praise = new Praise(user.getId() + 1, 1L);

        given(reviewRepository.findById(any())).willReturn(Optional.of(review));
        given(praiseRepository.existsByUserIdAndReviewId(any(), any())).willReturn(true);

        //then
        assertThrows(PraiseDuplicationException.class, () -> {
            //when
            reviewService.praiseReview(1L, request);
        });
    }

    @Test
    @DisplayName("'????????? ??????' ??? ????????? ??? ??????.")
    public void testRemovePraiseReview() throws Exception {
        //given
        RequestRemovePraiseReview request = new RequestRemovePraiseReview(user.getId() + 1);

        Review review = Review.of(user, item, 5, "???????????? ?????? ???????????????. ????????? ?????? ???????????? ????????????.");
        Praise praise = new Praise(user.getId() + 1, 1L);
        review.addHelp();

        given(reviewRepository.findById(any())).willReturn(Optional.of(review));
        given(praiseRepository.findByIdAndUserIdAndReviewId(any(), any(), any())).willReturn(Optional.of(praise));

        //when
        reviewService.deletePraise(1L, 1L, request);

        //then
        assertThat(review.getHelp()).isEqualTo(0);
    }



}