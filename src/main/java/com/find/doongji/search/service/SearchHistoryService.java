package com.find.doongji.search.service;

import com.find.doongji.search.payload.SearchHistory;

import java.util.List;

public interface SearchHistoryService {

    void addSearchHistory(SearchHistory searchHistory);

    List<SearchHistory> getSearchHistory(String username);

    void removeSearchHistory(Long id);
}
