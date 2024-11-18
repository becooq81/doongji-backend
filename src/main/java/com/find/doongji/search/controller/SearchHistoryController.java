package com.find.doongji.search.controller;

import com.find.doongji.search.payload.request.SearchHistoryRequest;
import org.springframework.http.ResponseEntity;

public interface SearchHistoryController {

    /**
     * Adds a new search history record for the given user.
     * @param username The username of the user who made the search.
     * @param searchHistoryRequest
     * @return ResponseEntity with status code 201 (Created) upon successful creation.
     */
    ResponseEntity<?> addSearchHistory(String username, SearchHistoryRequest searchHistoryRequest);

    /**
     * Retrieves all search history records for a specific user, ordered by the most recent search.
     * @param username The username whose search history is to be fetched.
     * @return ResponseEntity with status code 200 (OK) and a list of search history records.
     */
    ResponseEntity<?> getSearchHistoryByUsername(String username);

    /**
     * Deletes a specific search history record by its unique ID.
     *
     * @param id The ID of the search history record to be deleted.
     * @return ResponseEntity with status code 200 (OK) upon successful deletion.
     */
    ResponseEntity<Void> deleteSearchHistoryById(Long id);
}
