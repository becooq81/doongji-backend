package com.find.doongji.history.service;

import com.find.doongji.auth.enums.Role;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.history.repository.HistoryRepository;
import com.find.doongji.member.payload.request.MemberEntity;
import com.find.doongji.member.repository.MemberRepository;
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
public class HistoryServiceTest {

    @Autowired
    HistoryService service;

    @Autowired
    HistoryRepository repo;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        String username = "testuser";
        MemberEntity memberEntity = MemberEntity.builder()
                .username(username)
                .email("test@gmail.com")
                .name("Test User")
                .role(Role.ROLE_USER.getKey())
                .password("password")
                .build();
        memberRepository.insertMember(memberEntity);
    }

    @Test
    void addSearchHistoryTest_success() {
        // Given
        String username = "testuser";
        String query = "New search query";
        HistoryRequest searchHistoryRequest = new HistoryRequest(username, query);

        // When
        service.addHistory(searchHistoryRequest);

        // Then
        List<HistoryResponse> searchHistories = repo.getHistoryByUsername(username);
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
        service.addHistory(searchHistoryRequest);

        // When: Trying to add the same search history again
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.addHistory(searchHistoryRequest);
        });

        // Then
        assertEquals("Duplicate search detected within a short period", exception.getMessage());
    }


}
