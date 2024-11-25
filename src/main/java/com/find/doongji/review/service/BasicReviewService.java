package com.find.doongji.review.service;

import com.find.doongji.auth.service.AuthService;
import com.find.doongji.openai.client.OpenAIClient;
import com.find.doongji.review.payload.request.ReviewCreateRequest;
import com.find.doongji.review.repository.ReviewRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final AuthService authService;

    private final OpenAIClient openAIClient;

    @Override
    @Transactional
    public void createReview(ReviewCreateRequest request) throws  Exception {
        if (!authService.isAuthenticated()) {
            throw new Exception("User must be authenticated to write reviews.");
        }

        reviewRepository.insertReview(request);

    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getReviewsByAptSeq(String aptSeq) throws Exception {
        List<String> reviews = reviewRepository.selectReviewsByAptSeq(aptSeq);

        if (reviews == null || reviews.isEmpty()) {
            return new ArrayList<>();
        }
        return reviews;

    }

    @Override
    public String summarize(String aptSeq) throws Exception {
        List<String> reviews = getReviewsByAptSeq(aptSeq);
        if (reviews.isEmpty()) {
            throw new Exception("No reviews found for the given aptSeq.");
        }
        String summaryPrompt = "다음 리뷰들에 대해 예의있는 말투로 250자 이내로 요약해주십시오." + String.join(", ", reviews);

        String overview = openAIClient.chat(summaryPrompt);
        if (overview.isEmpty()) {
            throw new Exception("ChatGPT failed to summarize reviews.");
        }
        return overview;
    }

    @Override
    public void loadReviewsFromData(String filePath) throws Exception {

        if (reviewRepository.checkIfTableExists() != 0) {
            System.out.println("Table of crawled reviews already exists.");
            return;
        }

        System.out.println("Table of crawled reviews does not exist. Creating table..." + System.currentTimeMillis());

        try (CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("EUC-KR"))))) {
            String[] columns;
            List<ReviewCreateRequest> requests = new ArrayList<>();
            reader.readNext(); // Skip header

            while ((columns = reader.readNext()) != null) {
                try {
                    String aptSeq = columns[0];
                    String totalDesc = columns[3];
                    String trafficDesc = columns[4];
                    String aroundDesc = columns[5];
                    String careDesc = columns[6];
                    String residentDesc = columns[7];


                    String[] descriptions = {totalDesc, trafficDesc, aroundDesc, careDesc, residentDesc};

                    for (String desc : descriptions) {
                        requests.add(createEntity(aptSeq, desc));
                    }

                    if (requests.size() >= 100) {
                        reviewRepository.bulkInsertReview(requests);
                        requests.clear();
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + Arrays.toString(columns) + " | Error: " + e.getMessage());
                }
            }

            // Insert remaining entities
            if (!requests.isEmpty()) {
                reviewRepository.bulkInsertReview(requests);
            }
        }


        System.out.println("Table of crawled reviews created." + System.currentTimeMillis());
    }

    private ReviewCreateRequest createEntity(String aptSeq, String content) {
        return ReviewCreateRequest.builder()
                .aptSeq(aptSeq)
                .content(content)
                .build();
    }
}
