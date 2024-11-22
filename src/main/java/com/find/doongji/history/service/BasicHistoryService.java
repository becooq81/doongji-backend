package com.find.doongji.history.service;

import com.find.doongji.auth.service.AuthService;
import com.find.doongji.history.payload.request.HistoryRequest;
import com.find.doongji.history.payload.response.HistoryResponse;
import com.find.doongji.history.repository.HistoryRepository;
import com.find.doongji.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicHistoryService implements HistoryService {

    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;


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
     * @return list of history entries for the specified user
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistoryResponse> getAllHistory() {
        if (!authService.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!memberRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        validateAuthenticatedUser(username);
        return historyRepository.getHistoryByUsername(username);
    }

    /**
     * Removes a history entry by its ID.
     *
     * @param id the ID of the history entry to be removed
     */
    @Override
    public void removeHistory(String username, Long id) {
        if (!memberRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User does not exist");
        }
        historyRepository.deleteHistoryByUsernameAndId(username, id);
    }


    private void validateRequest(HistoryRequest request) {
        if (!memberRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("User does not exist");
        }

        if (!authService.isAuthenticated() || !SecurityContextHolder.getContext().getAuthentication().getName().equals(request.getUsername())) {
            throw new IllegalArgumentException("User is not authorized to add history for another user");
        }
    }

    private void validateAuthenticatedUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        String authenticatedUsername = authentication.getName();
        if (!authenticatedUsername.equals(username)) {
            throw new IllegalArgumentException("User is not authorized for this operation");
        }
    }
}
