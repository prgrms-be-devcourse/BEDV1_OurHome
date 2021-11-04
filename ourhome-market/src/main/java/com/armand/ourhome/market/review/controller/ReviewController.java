package com.armand.ourhome.market.review.controller;

import com.armand.ourhome.market.review.dto.request.*;
import com.armand.ourhome.market.review.dto.response.ResponseAddReview;
import com.armand.ourhome.market.review.dto.response.ResponseUpdateReview;
import com.armand.ourhome.market.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ResponseAddReview> addReview(@Valid @RequestBody RequestAddReview request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.save(request));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ResponseUpdateReview> updateReview(@PathVariable("reviewId") Long reviewId,
                                                             @Valid @RequestBody RequestUpdateReview request) {
        return ResponseEntity.ok(reviewService.update(reviewId, request));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("reviewId") Long reviewId,
                                             @Valid @RequestBody RequestDeleteReview request) {
        reviewService.delete(reviewId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reviewId}/images")
    public ResponseEntity<Void> deleteReviewImage(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReviewImageBy(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reviewId}/praise")
    public ResponseEntity<Long> praiseReview(@PathVariable("reviewId") Long reviewId, @Valid @RequestBody RequestPraiseReview request) {

        return ResponseEntity.ok(reviewService.praiseReview(reviewId, request));
    }

    @DeleteMapping("/{reviewId}/praise/{praiseId}")
    public ResponseEntity<Void> deletePraiseReview(@PathVariable("reviewId") Long reviewId,
                                                   @PathVariable("praiseId") Long praiseId,
                                                   @Valid @RequestBody RequestRemovePraiseReview request) {
        reviewService.deletePraise(praiseId, reviewId, request);

        return ResponseEntity.noContent().build();
    }
}
