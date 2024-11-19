package com.find.doongji.history.repository;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.user.payload.request.SignUpRequest;
import com.find.doongji.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Transactional
@SpringBootTest
public class HistoryRepositoryTest {


    @Autowired
    HistoryRepository historyRepository;

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

        HistoryRequest searchHistoryRequest = new HistoryRequest(username, query);
        historyRepository.insertHistory(searchHistoryRequest);

        List<HistoryResponse> searchHistories = historyRepository.getHistoryByUsername(username);
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

        HistoryRequest searchHistoryRequest = new HistoryRequest(
                username,
                query
        );

        historyRepository.insertHistory(searchHistoryRequest);

        Optional<HistoryResponse> duplicateSearchHistory = historyRepository.findDuplicateHistory(username, query);
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

        historyRepository.insertHistory(new HistoryRequest(username, "First search"));
        historyRepository.insertHistory(new HistoryRequest(username, "Second search"));

        List<HistoryResponse> searchHistories = historyRepository.getHistoryByUsername(username);

        Assertions.assertNotNull(searchHistories);
        for (HistoryResponse sh : searchHistories) {
            log.info("Search history: {}", sh);
        }
        Assertions.assertEquals(2, searchHistories.size());
        Assertions.assertTrue(searchHistories.stream().anyMatch(sh -> "First search".equals(sh.getQuery())));
        Assertions.assertTrue(searchHistories.stream().anyMatch(sh -> "Second search".equals(sh.getQuery())));
    }

}