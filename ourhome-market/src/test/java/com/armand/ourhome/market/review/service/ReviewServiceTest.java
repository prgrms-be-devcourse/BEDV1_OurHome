package com.armand.ourhome.market.review.service;

import com.armand.ourhome.common.error.exception.InvalidValueException;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.order.repository.OrderItemRepository;
import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.dto.request.RequestAddReview;
import com.armand.ourhome.market.review.exception.UserAccessDeniedException;
import com.armand.ourhome.market.review.repository.ReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    @DisplayName("리뷰를 저장한다")
    public void testSaveReview() throws Exception {
        //given
        RequestAddReview request = RequestAddReview.builder()
                .itemId(1L)
                .comment("너무나도 좋은 제품입니다. 다음에도 다시 구매하고 싶습니다!")
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
    @DisplayName("사용자는 상품 리뷰를 하나만 생성할 수 있다")
    public void testSaveReviewAlreadyExist() throws Exception {
        //given
        RequestAddReview request = RequestAddReview.builder()
                .itemId(1L)
                .comment("너무나도 좋은 제품입니다. 다음에도 다시 구매하고 싶습니다!")
                .rating(4)
                .userId(1L)
                .build();

        Review review = Review.of(user, item, request.getRating(), request.getComment());

        given(orderItemRepository.existsOrderItemByUserIdAndItemId(any(), any())).willReturn(true);
        given(reviewRepository.existsByItemIdAndUserId(any(), any())).willReturn(true);

        //then
        Assertions.assertThrows(InvalidValueException.class, () -> {
            //when
            reviewService.save(request);
        });
    }

    @Test
    @DisplayName("리뷰를 저장한다")
    public void testSaveReviewNotOrderItem() throws Exception {
        //given
        RequestAddReview request = RequestAddReview.builder()
                .itemId(1L)
                .comment("너무나도 좋은 제품입니다. 다음에도 다시 구매하고 싶습니다!")
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
}