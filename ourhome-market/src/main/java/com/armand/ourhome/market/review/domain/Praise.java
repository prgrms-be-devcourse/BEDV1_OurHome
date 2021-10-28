package com.armand.ourhome.market.review.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "praise")
public class Praise {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    public Praise(Long userId, Long reviewId) {

        Assert.notNull(userId, "userId는 null이 될 수 없습니다.");
        Assert.notNull(reviewId, "reviewId는 null이 될 수 없습니다.");

        this.userId = userId;
        this.reviewId = reviewId;
    }
}
