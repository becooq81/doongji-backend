package com.find.doongji.search.payload.response;

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

}
