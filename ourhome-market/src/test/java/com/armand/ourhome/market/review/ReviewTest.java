package com.armand.ourhome.market.review;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.review.domain.Review;
import com.armand.ourhome.market.review.domain.ReviewStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewTest {

    private User user = User.builder().build();
    private Item item = Item.builder()
            .name("name")
            .imageUrl("imageUrl")
            .description("description")
            .category(Category.DAILY_NECESSITIES)
            .company(new Company("company"))
            .price(1000)
            .stockQuantity(100)
            .build();

    @Test
    @DisplayName("리뷰를 생성할 수 있다")
    public void testCreateReview() throws Exception {
        String comment = "제품이 너무나도 좋습니다. 다음에 또 구매하겠습니다";
        Review review = Review.of(user, item, 5, comment);

        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getComment()).isEqualTo(comment);
        assertThat(review.getStatus()).isEqualTo(ReviewStatus.POSTED);
    }

    @Test
    @DisplayName("리뷰 평점은 0이상이다.")
    public void testCreateReviewUnderZero() throws Exception {
        String comment = "제품이 너무나도 좋습니다. 다음에 또 구매하겠습니다";

        assertThrows(IllegalArgumentException.class, () -> {
            Review review = Review.of(user, item, -1, comment);
        });
    }


    @Test
    @DisplayName("리뷰 평점은 5이하 이다.")
    public void testCreateReviewOver5() throws Exception {
        String comment = "제품이 너무나도 좋습니다. 다음에 또 구매하겠습니다";

        assertThrows(IllegalArgumentException.class, () -> {
            Review review = Review.of(user, item, 6, comment);
        });
    }


    @Test
    @DisplayName("리뷰 코멘트 길이는 20자 이상이다.")
    public void testCreateReviewCommentUnder20() throws Exception {
        String comment = "제품이 너무나도 좋습니다";

        assertThrows(IllegalArgumentException.class, () -> {
            Review review = Review.of(user, item, 4, comment);
        });
    }

    @Test
    @DisplayName("리뷰를 수정할 수 있다")
    public void testUpdateReview() throws Exception {
        // given
        String comment = "제품이 너무나도 좋습니다. 다음에 또 구매하겠습니다";
        Review review = Review.of(user, item, 5, comment);

        String updatedComment = "수정된 리뷰입니다. 제품이 너무나도 좋습니다. 다음에 또 구매하겠습니다.";
        int updateRating = 1;

        // when
        review.update(updatedComment, updateRating);

        // then
        assertThat(review.getRating()).isEqualTo(updateRating);
        assertThat(review.getComment()).isEqualTo(updatedComment);
    }

    @Test
    @DisplayName("리뷰를 삭제할 수 있다")
    public void testDelete1Review() throws Exception {
        // given
        String comment = "제품이 너무나도 좋습니다. 다음에 또 구매하겠습니다";
        Review review = Review.of(user, item, 5, comment);

        // when
        review.delete();

        // then
        assertThat(review.getStatus()).isEqualTo(ReviewStatus.DELETED);
    }

}