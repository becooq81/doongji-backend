package com.find.doongji.history.controller;

import com.find.doongji.search.payload.request.SearchRequest;
import org.springframework.http.ResponseEntity;

public interface HistoryController {


    /**
     * Retrieves all search history records for a specific user, ordered by the most recent search.
     * @param username The username whose search history is to be fetched.
     * @return ResponseEntity with status code 200 (OK) and a list of search history records.
     */
    ResponseEntity<?> getHistoryByUsername(String username);

    /**
     * Deletes a specific search history record by its unique ID.
     *
     * @param id The ID of the search history record to be deleted.
     * @return ResponseEntity with status code 200 (OK) upon successful deletion.
     */
    ResponseEntity<Void> deleteHistoryById(String username, Long id);
}
