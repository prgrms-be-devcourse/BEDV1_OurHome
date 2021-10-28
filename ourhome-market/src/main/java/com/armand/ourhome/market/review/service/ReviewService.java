package com.armand.ourhome.market.review.service;

import com.armand.ourhome.market.review.repository.ReviewRepository;
import com.armand.ourhome.market.review.service.dto.RequestAddReview;
import com.armand.ourhome.market.review.service.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

}
