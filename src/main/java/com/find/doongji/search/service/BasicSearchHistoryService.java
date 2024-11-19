package com.find.doongji.search.service;

import com.find.doongji.search.payload.request.SearchHistoryRequest;
import com.find.doongji.search.payload.response.SearchHistoryResponse;
import com.find.doongji.search.repository.SearchHistoryRepository;
import com.find.doongji.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicSearchHistoryService implements SearchHistoryService{

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;


    /**
     * Adds a new search history entry to the database.
     *
     * @param searchHistoryRequest the search history entry to be added
     */
    @Override
    @Transactional
    public void addSearchHistory(SearchHistoryRequest searchHistoryRequest) {
        if (!userRepository.existsByUsername(searchHistoryRequest.getUsername())) {
            throw new IllegalArgumentException("User does not exist");
        }

        if (isDuplicateSearch(searchHistoryRequest)) {
            throw new IllegalArgumentException("Duplicate search detected within a short period");
        }

        searchHistoryRepository.insertSearchHistory(searchHistoryRequest);
    }

    /**
     * Retrieves search history entries for a specific user.
     *
     * @param username the username for which to retrieve search history
     * @return list of search history entries for the specified user
     */
    @Override
    public List<SearchHistoryResponse> getSearchHistory(String username) {
        return searchHistoryRepository.getSearchHistoryByUsername(username);
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
        searchHistoryRepository.deleteSearchHistoryByUsernameAndId(username, id);
    }

    private boolean isDuplicateSearch(SearchHistoryRequest searchHistoryRequest) {
        Optional<SearchHistoryResponse> existingSearch = searchHistoryRepository.findDuplicateSearchHistory(
                searchHistoryRequest.getUsername(),
                searchHistoryRequest.getQuery()
        );
        return existingSearch.isPresent();
    }
}
