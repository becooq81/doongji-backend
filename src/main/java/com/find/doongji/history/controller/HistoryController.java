package com.find.doongji.search.controller;

import com.find.doongji.search.payload.request.SearchHistoryRequest;
import com.find.doongji.search.payload.request.SearchRequest;
import org.springframework.http.ResponseEntity;

public interface SearchController {


    /**
     * Searches for recommendations based on the given query.
     * @param SearchRequest The search query.
     */
    ResponseEntity<?> search(SearchRequest request);

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
    ResponseEntity<Void> deleteSearchHistoryById(String username, Long id);
}
