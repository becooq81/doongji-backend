package com.find.doongji.history.service;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.history.repository.HistoryRepository;
import com.find.doongji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicHistoryService implements HistoryService {

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;


    /**
     * Adds a new history entry to the database.
     *
     * @param request the history entry to be added
     */
    @Override
    @Transactional
    public void addHistory(HistoryRequest request) {
        if (request == null || request.getUsername() == null || request.getQuery() == null) {
            throw new IllegalArgumentException("History request and its fields must not be null");
        }

        validateRequest(request);

        historyRepository.insertHistory(request);
    }

    /**
     * Retrieves history entries for a specific user.
     *
     * @param username the username for which to retrieve search history
     * @return list of history entries for the specified user
     */
    @Override
    public List<HistoryResponse> getAllHistory(String username) {
        return historyRepository.getHistoryByUsername(username);
    }

    /**
     * Removes a history entry by its ID.
     *
     * @param id the ID of the history entry to be removed
     */
    @Override
    public void removeHistory(String username, Long id) {
        if (!userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        historyRepository.deleteHistoryByUsernameAndId(username, id);
    }


    private void validateRequest(HistoryRequest request) {
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User does not exist");
        }

    }
}
