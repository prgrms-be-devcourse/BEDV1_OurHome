package com.armand.ourhome.market.review.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_image")
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewImageStatus status;

    private ReviewImage(Long reviewId, Long userId, String url) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.url = url;
        this.status = ReviewImageStatus.ACTIVE;
    }

    public static ReviewImage of(Long reviewId, Long userId, String url) {
        Assert.notNull(reviewId, "Review.id는 null이 될 수 없습니다.");
        Assert.notNull(url, "Review 이미지 URL은 null이 될 수 없습니다");
        Assert.notNull(userId, "User.id는 null이 될 수 없습니다.");

        return new ReviewImage(reviewId, userId, url);
    }

    //== 비즈니스 메서드 ==//
    public void update(String url) {
        Assert.notNull(url, "Review 이미지 URL은 null이 될 수 없습니다");
        this.url = url;
    }

    public void delete() {
        this.status = ReviewImageStatus.INACTIVE;
    }

    public boolean isWrittenBy(Long userId) {
        return Objects.equals(this.userId, userId);
    }
}
