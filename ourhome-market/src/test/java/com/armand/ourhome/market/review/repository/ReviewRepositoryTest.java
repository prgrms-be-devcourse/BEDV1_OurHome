package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.domain.ItemDetail;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.review.domain.Review;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@Import(OurHomeDomainConfig.class)
class ReviewRepositoryTest {

//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private List<User> users = new ArrayList<>();
//    private List<Review> reviews = new ArrayList<>();
//    private Item item = null;
//    @BeforeAll
//    void setup() {
//        item = Item.builder()
//            .name("item")
//            .description("description")
//            .imageUrl("imageUrl")
//            .category(Category.DAILY_NECESSITIES)
//            .company(new Company("company"))
//            .price(1000)
//            .stockQuantity(100)
//            .build();
//
//        for (int i = 0; i < 1000; i++) {
//            User user = new User();
//            users.add(user);
//            Review review = Review.of(user, item, i % 6, String.format("정말 좋은 제품입니다. 다음에도 꼭 다시 구매하고 싶은 욕구가 샘솟습니다.{%s}", i));
//            reviews.add(review);
//        }
//    }
//
//    @Test
//    public void testAutowired() throws Exception {
//        assertThat(reviewRepository).isNotNull();
//    }
//
//    @Test
//    @DisplayName("리뷰가 저장된다.")
//    public void testSaveReview() throws Exception {
//        //given
//        Review review = reviews.get(0);
//
//        //when
//        reviewRepository.save(review);
//
//        //then
//        Optional<Review> actual = reviewRepository.findById(review.getId());
//        assertThat(actual).isPresent();
//        assertThat(actual.get().getComment()).isEqualTo(review.getComment());
//    }
}