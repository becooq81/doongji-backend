package com.find.doongji.search.repository;

import com.find.doongji.search.payload.request.SearchHistoryRequest;
import com.find.doongji.search.payload.response.SearchHistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SearchHistoryRepository {

    /**
     * Insert a new search history record.
     * @param searchHistoryRequest The SearchHistory object containing search details.
     */
    void insertSearchHistory(SearchHistoryRequest searchHistoryRequest);

    /**
     * Retrieve search history for a specific username.
     * @param username The username for which to retrieve search history.
     * @return A list of SearchHistory records.
     */
    List<SearchHistoryResponse> getSearchHistoryByUsername(String username);

    /**
     * Delete a search history record by its ID.
     * @param username The username of the user who owns the search history.
     * @param id The ID of the search history to delete.
     */
    void deleteSearchHistoryByUsernameAndId(@Param("username") String username, @Param("id") Long id);

    /**
     * Find a duplicate search history record.
     * @param username The username to check for duplicates
     * @param query The search query to check for duplicates
     * @return Optional containing the duplicate SearchHistoryResponse if found
     */
    Optional<SearchHistoryResponse> findDuplicateSearchHistory(
        @Param("username") String username,
        @Param("query") String query
    );
}
