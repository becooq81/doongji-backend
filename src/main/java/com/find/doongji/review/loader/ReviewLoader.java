package com.find.doongji.review.loader;

import com.find.doongji.review.repository.ReviewRepository;
import com.find.doongji.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewLoader implements CommandLineRunner {

    private final ReviewService reviewService;

    @Override
    public void run(String... args) throws Exception {

        reviewService.loadReviewsFromData("src/main/resources/reviews_data.csv");

    }
}
