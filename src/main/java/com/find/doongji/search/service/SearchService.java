package com.find.doongji.search.service;

import com.find.doongji.search.payload.request.SearchRequest;
import com.find.doongji.search.payload.response.SearchDetailResponse;
import com.find.doongji.search.payload.response.SearchResponse;
import com.find.doongji.search.payload.response.SearchResult;

import java.util.List;

public interface SearchService {

    List<SearchResponse> search(SearchRequest searchRequest, int page, int size) throws Exception;

    SearchDetailResponse viewSearched(String aptSeq) throws Exception;
}
