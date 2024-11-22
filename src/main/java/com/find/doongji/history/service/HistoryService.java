package com.find.doongji.history.service;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;

import java.util.List;

public interface HistoryService {

    /**
     * Adds a new history entry for a user
     *
     * @param searchHistoryRequest
     */
    void addHistory(HistoryRequest searchHistoryRequest);

    /**
     * Retrieves all history entries for a user
     *
     * @param username
     * @return
     */
    List<HistoryResponse> getAllHistory();

    /**
     * Removes a history entry by its ID
     *
     * @param username
     * @param id
     */
    void removeHistory(String username, Long id);
}
