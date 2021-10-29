package com.armand.ourhome.market.review.controller;

import com.armand.ourhome.market.review.service.ReviewService;
import com.armand.ourhome.market.review.service.dto.request.RequestAddReview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Long> addReview(@RequestBody RequestAddReview request) {
        Long reviewId = reviewService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }
}
