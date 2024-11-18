package com.find.doongji.search.repository;

import com.find.doongji.search.payload.SearchHistory;
import com.find.doongji.user.payload.request.SignUpRequest;
import com.find.doongji.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class SearchHistoryRepositoryTest {


    @Autowired
    SearchHistoryRepository searchHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void addSearchHistoryTest() {
        String username = "testuser";
        String query = "Test search query";

        SignUpRequest signUpRequest = new SignUpRequest(
                username,
                "test@gmail.com",
                "password",
                "password",
                "Test User"
        );
        userRepository.insertUser(signUpRequest);

        SearchHistory searchHistory = new SearchHistory(username, query);
        searchHistoryRepository.insertSearchHistory(searchHistory);

        List<SearchHistory> searchHistories = searchHistoryRepository.getSearchHistoryByUsername(username);
        Assertions.assertNotNull(searchHistories);
        Assertions.assertFalse(searchHistories.isEmpty());
        Assertions.assertEquals(query, searchHistories.get(0).getQuery());
    }

    @Test
    public void findDuplicateSearchHistoryTest() {
        String username = "testuser2";
        String query = "Duplicate search query";

        SignUpRequest signUpRequest = new SignUpRequest(
                username,
                "test@gmail.com",
                "password",
                "password",
                "Test User"
        );
        userRepository.insertUser(signUpRequest);

        SearchHistory searchHistory = new SearchHistory(
                username,
                query
        );

        searchHistoryRepository.insertSearchHistory(searchHistory);

        Optional<SearchHistory> duplicateSearchHistory = searchHistoryRepository.findDuplicateSearchHistory(username, query);
        Assertions.assertNotNull(duplicateSearchHistory);
    }

    @Test
    public void findSearchHistoryByUsernameTest() {
        String username = "testuser3";

        SignUpRequest signUpRequest = new SignUpRequest(
                username,
                "test@gmail.com",
                "password",
                "password",
                "Test User"
        );
        userRepository.insertUser(signUpRequest);

        searchHistoryRepository.insertSearchHistory(new SearchHistory(username, "First search"));
        searchHistoryRepository.insertSearchHistory(new SearchHistory(username, "Second search"));

        List<SearchHistory> searchHistories = searchHistoryRepository.getSearchHistoryByUsername(username);

        Assertions.assertNotNull(searchHistories);
        for (SearchHistory sh : searchHistories) {
            log.info("Search history: {}", sh);
        }
        Assertions.assertEquals(2, searchHistories.size());
        Assertions.assertTrue(searchHistories.stream().anyMatch(sh -> "First search".equals(sh.getQuery())));
        Assertions.assertTrue(searchHistories.stream().anyMatch(sh -> "Second search".equals(sh.getQuery())));
    }

}
