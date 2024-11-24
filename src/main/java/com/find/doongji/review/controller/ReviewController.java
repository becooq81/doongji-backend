package com.find.doongji.review.controller;

import com.find.doongji.review.payload.request.ReviewCreateRequest;
import org.springframework.http.ResponseEntity;

public interface ReviewController {

    ResponseEntity<?> createReview(ReviewCreateRequest request);
}
