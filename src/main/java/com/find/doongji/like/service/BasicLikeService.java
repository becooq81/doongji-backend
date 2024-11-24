package com.find.doongji.like.service;

import com.find.doongji.auth.service.AuthService;
import com.find.doongji.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicLikeService implements LikeService {

    private final LikeRepository likeRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public void toggleLike(String aptSeq) throws AccessDeniedException {

        validateRequest(aptSeq);
        if (!authService.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to like.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        likeRepository.toggleLike(username, aptSeq);
    }

    @Override
    @Transactional(readOnly = true)
    public int viewLike(String aptSeq) {

        validateRequest(aptSeq);
        if (!authService.isAuthenticated()) {
            return 0;
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return likeRepository.selectLike(username, aptSeq);
    }

    private void validateRequest(String aptSeq) {
        if (aptSeq == null || aptSeq.trim().isEmpty()) {
            throw new IllegalArgumentException("AptSeq must not be null or empty");
        }
    }
}
