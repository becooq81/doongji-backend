package com.find.doongji.review.controller;

import com.find.doongji.review.payload.request.ReviewCreateRequest;
import com.find.doongji.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/review")
public class BasicReviewController implements ReviewController{

    private final ReviewService reviewService;

    @Override
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        try {
            reviewService.createReview(request);
            return ResponseEntity.ok("Review created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    @PostMapping("/summary")
    public ResponseEntity<?> summarizeReview(@RequestParam String aptSeq) {
        try {
            return ResponseEntity.ok(reviewService.summarize(aptSeq));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
