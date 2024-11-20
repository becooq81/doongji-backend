package com.find.doongji.search.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private SearchResult searchResult;
    private SimilarityScore similarityScore;
}
