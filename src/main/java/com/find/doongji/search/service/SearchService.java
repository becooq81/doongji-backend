package com.find.doongji.search.service;

import com.find.doongji.apt.payload.response.SearchResult;
import com.find.doongji.search.payload.request.SearchRequest;

import java.util.List;

public interface SearchService {

    List<SearchResult> search(SearchRequest searchRequest) throws Exception;

}
