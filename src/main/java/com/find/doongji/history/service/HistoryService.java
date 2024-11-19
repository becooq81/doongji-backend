package com.find.doongji.history.service;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;

import java.util.List;

public interface SearchHistoryService {

    void addSearchHistory(HistoryRequest searchHistoryRequest);

    List<HistoryResponse> getSearchHistory(String username);

    void removeSearchHistory(String username, Long id);
}
