package com.find.doongji.search.service;

import com.find.doongji.search.payload.request.SearchHistoryRequest;
import com.find.doongji.search.payload.response.SearchHistoryResponse;

import java.util.List;

public interface SearchHistoryService {

    void addSearchHistory(SearchHistoryRequest searchHistoryRequest);

    List<SearchHistoryResponse> getSearchHistory(String username);

    void removeSearchHistory(String username, Long id);
}
