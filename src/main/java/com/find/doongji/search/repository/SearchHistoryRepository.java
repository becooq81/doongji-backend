package com.find.doongji.search.repository;

import com.find.doongji.search.payload.SearchHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SearchHistoryRepository {

    /**
     * Insert a new search history record.
     * @param searchHistory The SearchHistory object containing search details.
     */
    void insertSearchHistory(SearchHistory searchHistory);

    /**
     * Retrieve search history for a specific username.
     * @param username The username for which to retrieve search history.
     * @return A list of SearchHistory records.
     */
    List<SearchHistory> getSearchHistoryByUsername(String username);

    /**
     * Delete a search history record by its ID.
     * @param id The ID of the search history to delete.
     */
    void deleteSearchHistoryById(Long id);

    /**
     * Find a duplicate search history record.
     * @param username
     * @param query
     * @return SearchHistory IF a duplicate record is found, otherwise null.
     */
    Optional<SearchHistory> findDuplicateSearchHistory(String username, String query);
}
