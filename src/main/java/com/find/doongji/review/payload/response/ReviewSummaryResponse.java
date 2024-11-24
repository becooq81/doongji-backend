package com.find.doongji.review.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSummaryResponse {

    private String summary;
    private List<ReviewResponse> reviews;
}
