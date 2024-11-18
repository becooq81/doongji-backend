package com.find.doongji.search.service;

import com.find.doongji.search.payload.SearchHistory;
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
     * @param searchHistory the search history entry to be added
     */
    @Override
    @Transactional
    public void addSearchHistory(SearchHistory searchHistory) {
        if (!userRepository.existsByUsername(searchHistory.getUsername())) {
            throw new IllegalArgumentException("User does not exist");
        }

        if (isDuplicateSearch(searchHistory)) {
            throw new IllegalArgumentException("Duplicate search detected within a short period");
        }

        searchHistoryRepository.insertSearchHistory(searchHistory);
    }

    /**
     * Retrieves search history entries for a specific user.
     *
     * @param username the username for which to retrieve search history
     * @return list of search history entries for the specified user
     */
    @Override
    public List<SearchHistory> getSearchHistory(String username) {
        return searchHistoryRepository.getSearchHistoryByUsername(username);
    }

    /**
     * Removes a search history entry by its ID.
     *
     * @param id the ID of the search history entry to be removed
     */
    @Override
    public void removeSearchHistory(Long id) {
        searchHistoryRepository.deleteSearchHistoryById(id);
    }

    private boolean isDuplicateSearch(SearchHistory searchHistory) {
        Optional<SearchHistory> existingSearch = searchHistoryRepository.findDuplicateSearchHistory(
                searchHistory.getUsername(),
                searchHistory.getQuery()
        );
        return existingSearch.isPresent();
    }
}
