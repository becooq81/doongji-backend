package com.find.doongji.search.payload.response;

import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.search.enums.SimilarityScore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponse {
    private SimilarityScore similarityScore;
    private AptInfo aptInfo;
    private int isLiked;
}
