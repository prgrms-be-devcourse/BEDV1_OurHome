package com.armand.ourhome.market.review.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.text.MessageFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Embeddable
public class Rating {

    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 5;

    @Column(nullable = false)
    int rating;

    private Rating(int rating) {
        validateRating(rating);
        this.rating = rating;
    }

    private void validateRating(int rating) {
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new IllegalArgumentException(MessageFormat.format("잘못된 Rating 값입니다. rating = {0}", rating));
        }
    }

    public static Rating of(int rating) {
        return new Rating(rating);
    }
}
