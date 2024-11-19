package com.find.doongji.history.service;

import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.history.repository.HistoryRepository;
import com.find.doongji.user.payload.request.SignUpRequest;
import com.find.doongji.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
public class SearchHistoryServiceTest {

    @Autowired
    SearchHistoryService service;

    @Autowired
    HistoryRepository repo;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        String username = "testuser";
        SignUpRequest signUpRequest = new SignUpRequest(
                username,
                "test@gmail.com",
                "password",
                "password",
                "Test User"
        );
        userRepository.insertUser(signUpRequest);
    }

    @Test
    void addSearchHistoryTest_success() {
        // Given
        String username = "testuser";
        String query = "New search query";
        HistoryRequest searchHistoryRequest = new HistoryRequest(username, query);

        // When
        service.addSearchHistory(searchHistoryRequest);

        // Then
        List<HistoryResponse> searchHistories = repo.getSearchHistoryByUsername(username);
        assertNotNull(searchHistories);
        assertFalse(searchHistories.isEmpty());
        assertEquals(query, searchHistories.get(0).getQuery());
    }

    @Test
    void addSearchHistoryTest_duplicateSearch() {
        // Given
        String username = "testuser";
        String query = "Duplicate search query";
        HistoryRequest searchHistoryRequest = new HistoryRequest(username, query);

        // Add the first search history
        service.addSearchHistory(searchHistoryRequest);

        // When: Trying to add the same search history again
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addSearchHistory(searchHistoryRequest);
        });

        // Then
        assertEquals("Duplicate search detected within a short period", exception.getMessage());
    }

    @Test
    void getSearchHistoryTest() {
        // Given
        String username = "testuser";
        service.addSearchHistory(new HistoryRequest(username, "First search"));
        service.addSearchHistory(new HistoryRequest(username, "Second search"));

        // When
        List<HistoryResponse> searchHistories = service.getSearchHistory(username);

        // Then
        assertNotNull(searchHistories);
        assertEquals(2, searchHistories.size());
        assertTrue(searchHistories.stream().anyMatch(sh -> "First search".equals(sh.getQuery())));
        assertTrue(searchHistories.stream().anyMatch(sh -> "Second search".equals(sh.getQuery())));
    }


}
