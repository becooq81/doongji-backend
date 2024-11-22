package com.find.doongji.history.repository;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HistoryRepository {

    /**
     * Insert a new history record.
     *
     * @param request The History object containing search details.
     */
    void insertHistory(HistoryRequest request);

    /**
     * Retrieve  history for a specific username.
     *
     * @param username The username for which to retrieve  history.
     * @return A list of History records.
     */
    List<HistoryResponse> getHistoryByUsername(String username);

    /**
     * Delete a history record by its ID.
     *
     * @param username The username of the user who owns the history.
     * @param id       The ID of the history to delete.
     */
    void deleteHistoryByUsernameAndId(@Param("username") String username, @Param("id") Long id);

    /**
     * Find a duplicate history record.
     *
     * @param username The username to check for duplicates
     * @param query    The search query to check for duplicates
     * @return Optional containing the duplicate HistoryResponse if found
     */
    Optional<HistoryResponse> findDuplicateHistory(
            @Param("username") String username,
            @Param("query") String query
    );
}
