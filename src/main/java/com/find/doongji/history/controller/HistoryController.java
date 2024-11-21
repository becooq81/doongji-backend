package com.find.doongji.history.controller;

import com.find.doongji.search.payload.request.SearchRequest;
import org.springframework.http.ResponseEntity;

public interface HistoryController {


    /**
     * Retrieves all search history records for a specific user, ordered by the most recent search.
     * @return ResponseEntity with status code 200 (OK) and a list of search history records.
     */
    ResponseEntity<?> getHistoryByUsername();

}
