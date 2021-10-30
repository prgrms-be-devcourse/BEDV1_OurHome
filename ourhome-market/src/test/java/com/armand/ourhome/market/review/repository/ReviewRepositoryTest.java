package com.armand.ourhome.market.review.repository;

import com.armand.ourhome.domain.OurHomeDomainConfig;
import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.domain.ItemDetail;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.armand.ourhome.market.review.domain.Aggregate;
import com.armand.ourhome.market.review.domain.Rating;
import com.armand.ourhome.market.review.domain.Review;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(OurHomeDomainConfig.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private List<User> users = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private Item item = null;

    @BeforeEach
    void setup() {
        item = Item.builder()
            .name("item")
            .description("description")
            .imageUrl("imageUrl")
            .category(Category.DAILY_NECESSITIES)
            .company(new Company("company"))
            .price(1000)
            .stockQuantity(100)
            .build();

        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .email("email" + i)
                    .password("password" + i)
                    .nickname("nickname" + i)
                    .build();
            users.add(user);
            Review review = Review.of(user, item, i % 6, String.format("정말 좋은 제품입니다. 다음에도 꼭 다시 구매하고 싶은 욕구가 샘솟습니다.{%s}", i));
            reviews.add(review);
        }
    }

    @Test
    public void testAutowired() throws Exception {
        assertThat(reviewRepository).isNotNull();
    }

    @Test
    @DisplayName("리뷰가 저장된다.")
    public void testSaveReview() throws Exception {
        //given
        Review review = reviews.get(0);

        //when
        reviewRepository.save(review);

        //then
        Optional<Review> actual = reviewRepository.findById(review.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get().getComment()).isEqualTo(review.getComment());
    }

    @Test
    @DisplayName("상품의 리뷰 수와 평점 평균을 알 수 있다.")
    public void testGetAggregate() throws Exception {
        //given
        double avg = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .getAsDouble();

        userRepository.saveAll(users);
        itemRepository.save(item);
        reviewRepository.saveAll(reviews);

        //when
        Aggregate aggregate = reviewRepository.countReviewAndAverageRatingOf(item);

        //then
        assertThat(aggregate.getCount()).isEqualTo(100);
        assertThat(aggregate.getAverage()).isEqualTo(avg);
    }

    @Test
    @DisplayName("리뷰를 평점에 따라 조회할 수 있다")
    public void findByItemAndRatingOrderById() throws Exception {
        //given
        int size = 3;
        userRepository.saveAll(users);
        itemRepository.save(item);
        reviewRepository.saveAll(reviews);
        Rating rating = Rating.of(5);


        //when
        Page<Review> page = reviewRepository.findByItemIdAndRatingOrderById(item.getId(), rating, PageRequest.of(0, size));

        //then
        List<Integer> ratings = page.getContent().stream().map(Review::getRating).collect(Collectors.toList());
        assertThat(ratings).containsExactly(rating.getRating(), rating.getRating(), rating.getRating());
    }

    @Test
    @DisplayName("User.id와 Item.id로 리뷰가 존재하는 지 확인할 수 있다.")
    public void testExistsByItemIdAndUserId() throws Exception {
        // given
        userRepository.saveAll(users);
        itemRepository.save(item);
        reviewRepository.saveAll(reviews);

        Long userId = users.get(0).getId();
        Long itemId = item.getId();

        //when
        boolean exists1 = reviewRepository.existsByItemIdAndUserId(itemId, userId);
        boolean exists2 = reviewRepository.existsByItemIdAndUserId(itemId + 10000, userId + 10000);

        //then
        assertThat(exists1).isTrue();
        assertThat(exists2).isFalse();
    }


}