package com.armand.ourhome.market.review.controller;

import com.armand.ourhome.market.review.dto.request.RequestDeleteReview;
import com.armand.ourhome.market.review.dto.request.RequestUpdateReview;
import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.dto.request.RequestAddReview;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    public ResponseEntity<Long> addReview(@Valid @RequestBody RequestAddReview request) {
        Long reviewId = reviewService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Long> updateReview(@PathVariable("reviewId") Long reviewId,
                                             @Valid @RequestBody RequestUpdateReview request) {
        return ResponseEntity.ok(reviewService.update(reviewId, request));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("reviewId") Long reviewId,
                                             @RequestBody RequestDeleteReview request) {
        reviewService.delete(reviewId, request);
        return ResponseEntity.noContent().build();
    }
}
