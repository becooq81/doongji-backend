package com.find.doongji.review.service;

import com.find.doongji.address.payload.response.AddressMappingResponse;
import com.find.doongji.address.repository.AddressRepository;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.openai.client.OpenAIClient;
import com.find.doongji.review.payload.request.ReviewCreateRequest;
import com.find.doongji.review.payload.request.ReviewEntity;
import com.find.doongji.review.payload.response.ReviewResponse;
import com.find.doongji.review.payload.response.ReviewSummaryResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final AddressRepository addressRepository;

    private final AuthService authService;

    private final OpenAIClient openAIClient;

    @Override
    @Transactional
    public void createReview(ReviewCreateRequest request) throws  Exception {
        if (!authService.isAuthenticated()) {
            throw new Exception("User must be authenticated to write reviews.");
        }

        // TODO: 단지 ID와 apt seq 관계 재정의
        List<AddressMappingResponse> addressMappings = addressRepository.selectAddressMappingByAptSeq(request.getAptSeq());
        for (AddressMappingResponse addressMapping: addressMappings) {
            ReviewEntity entity = ReviewEntity.builder()
                    .danjiId(addressMapping.getDanjiId())
                    .description(request.getContent())
                    .aptSeq(request.getAptSeq())
                    .build();
            reviewRepository.insertReview(entity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewSummaryResponse getReviewsByAptSeq(String aptSeq) throws Exception {
        List<ReviewResponse> reviews =  reviewRepository.selectReviewsByAptSeq(aptSeq);

        if (reviews.isEmpty()) {
            throw new Exception("No reviews found for aptSeq: " + aptSeq);
        }

        String summaryPrompt = "다음 리뷰들에 대해 예의있는 말투로 250자 이내로 요약해주십시오." + reviews.stream()
                .map(ReviewResponse::getDescription) // Extract description
                .collect(Collectors.joining(", "));

        String overview = openAIClient.chat(summaryPrompt);

        return ReviewSummaryResponse.builder()
                .summary(overview)
                .reviews(reviews)
                .build();

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
            List<ReviewEntity> entities = new ArrayList<>();
            reader.readNext(); // Skip header

            while ((columns = reader.readNext()) != null) {
                try {
                    Long danjiId = Long.parseLong(columns[0]);
                    String totalDesc = columns[3];
                    String trafficDesc = columns[4];
                    String aroundDesc = columns[5];
                    String careDesc = columns[6];
                    String residentDesc = columns[7];

                    AddressMappingResponse addressMapping = addressRepository.selectAddressMappingByDanjiId(danjiId);

                    if (addressMapping == null) {
                        System.out.println("Address mapping not found for danjiId: " + danjiId);
                        continue;
                    }

                    String aptSeq = addressMapping.getAptSeq();
                    String[] descriptions = {totalDesc, trafficDesc, aroundDesc, careDesc, residentDesc};

                    for (String desc : descriptions) {
                        entities.add(createEntity(danjiId, desc, aptSeq));
                    }

                    if (entities.size() >= 100) {
                        reviewRepository.bulkInsertReview(entities);
                        entities.clear();
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + Arrays.toString(columns) + " | Error: " + e.getMessage());
                }
            }

            // Insert remaining entities
            if (!entities.isEmpty()) {
                reviewRepository.bulkInsertReview(entities);
            }
        }


        System.out.println("Table of crawled reviews created." + System.currentTimeMillis());
    }

    private ReviewEntity createEntity(Long danjiId,String description, String aptSeq) {
        return ReviewEntity.builder()
                .danjiId(danjiId)
                .description(description)
                .aptSeq(aptSeq)
                .build();
    }
}
