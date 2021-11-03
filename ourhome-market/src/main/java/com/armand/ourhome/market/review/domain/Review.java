package com.armand.ourhome.market.review.domain;

import com.armand.ourhome.domain.base.BaseEntity;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.market.review.exception.UserAccessDeniedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    private static final int MIN_COMMENT_LENGTH = 20;
    private static final int MAX_COMMENT_LENGTH = 255;

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Embedded
    private Rating rating;

    @Column(nullable = false, length = MAX_COMMENT_LENGTH)
    private String comment;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(nullable = false)
    private int help;

    @Builder
    public Review(User user, Item item, int rating, String comment) {

        Assert.notNull(user, "사용자는 null이 될 수 없습니다.");
        Assert.notNull(item, "상품은 null이 될 수 없습니다.");

        validateComment(comment);

        this.user = user;
        this.item = item;
        this.rating = Rating.of(rating);
        this.comment = comment;
        this.status = ReviewStatus.POSTED;
        this.help = 0;
    }

    public static Review of
            (User user, Item item, int rating, String comment) {
        return new Review(user, item, rating, comment);
    }

    //== Business Method ==//
    public void update(String comment, int rating) {
        validateComment(comment);

        this.comment = comment;
        this.rating = Rating.of(rating);
    }

    public void delete() {
        this.status = ReviewStatus.DELETED;
    }

    public int getRating() {
        return this.rating.getRating();
    }

    public void addHelp() {
        this.help += 1;
    }

    public void removeHelp() {
        if (help == 0) {
            throw new IllegalArgumentException("help가 이미 0입니다.");
        }
        this.help -= 1;
    }

    //== Validation Method ==//
    private void validateComment(String comment) {
        Assert.notNull(comment, "리뷰 comment는 null이 될 수 없습니다.");

        if (comment.length() > MAX_COMMENT_LENGTH || comment.length() < MIN_COMMENT_LENGTH) {
            throw new IllegalArgumentException(MessageFormat.format("comment의 길이는 최소 20이상 255자 이하입니다. comment = {0}", comment));
        }
    }

    public boolean isWrittenBy(Long userId ) {
        return Objects.equals(this.user.getId(), userId);
    }
}
