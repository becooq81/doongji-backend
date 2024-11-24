package com.find.doongji.search.payload.response;

import com.find.doongji.review.payload.response.ReviewSummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDetailResponse {

    private SearchResult searchResult;
    private int isLiked;
    private ReviewSummaryResponse overview;


}
