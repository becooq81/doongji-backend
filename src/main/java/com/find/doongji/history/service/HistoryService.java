package com.find.doongji.history.service;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;

import java.util.List;

public interface HistoryService {

    void addHistory(HistoryRequest searchHistoryRequest);

    List<HistoryResponse> getAllHistory(String username);

    void removeHistory(String username, Long id);
}
