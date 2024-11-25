package com.find.doongji.review.service;

import com.find.doongji.review.payload.request.ReviewCreateRequest;
import com.find.doongji.review.payload.response.ReviewSummaryResponse;

import java.util.List;

public interface ReviewService {

    void loadReviewsFromData(String path) throws Exception;

    void createReview(ReviewCreateRequest request) throws Exception;

    ReviewSummaryResponse getReviewsByAptSeq(String aptSeq) throws Exception;
}