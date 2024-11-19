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
public class BasicSearchHistoryService implements SearchHistoryService{

    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;


    /**
     * Adds a new search history entry to the database.
     *
     * @param searchHistoryRequest the search history entry to be added
     */
    @Override
    @Transactional
    public void addSearchHistory(HistoryRequest searchHistoryRequest) {
        if (searchHistoryRequest == null || searchHistoryRequest.getUsername() == null || searchHistoryRequest.getQuery() == null) {
            throw new IllegalArgumentException("Search history request and its fields must not be null");
        }

        validateRequest(searchHistoryRequest);

        historyRepository.insertSearchHistory(searchHistoryRequest);
    }

    /**
     * Retrieves search history entries for a specific user.
     *
     * @param username the username for which to retrieve search history
     * @return list of search history entries for the specified user
     */
    @Override
    public List<HistoryResponse> getSearchHistory(String username) {
        return historyRepository.getSearchHistoryByUsername(username);
    }

    /**
     * Removes a search history entry by its ID.
     *
     * @param id the ID of the search history entry to be removed
     */
    @Override
    public void removeSearchHistory(String username, Long id) {
        if (!userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        historyRepository.deleteSearchHistoryByUsernameAndId(username, id);
    }

    private boolean isDuplicateSearch(HistoryRequest searchHistoryRequest) {
        Optional<HistoryResponse> existingSearch = historyRepository.findDuplicateSearchHistory(
                searchHistoryRequest.getUsername(),
                searchHistoryRequest.getQuery()
        );
        return existingSearch.isPresent();
    }

    private void validateRequest(HistoryRequest request) {
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User does not exist");
        }

        if (isDuplicateSearch(request)) {
            throw new IllegalArgumentException("Duplicate search detected within a short period");
        }
    }
}
