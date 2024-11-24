package com.find.doongji.like.service;

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

    @Override
    @Transactional
    public void toggleLike(String aptSeq) throws AccessDeniedException {
        if (!isLoggedIn()) {
            throw new AccessDeniedException("You must be logged in to like.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        likeRepository.toggleLike(username, aptSeq);
    }

    @Override
    @Transactional(readOnly = true)
    public int viewLike(String aptSeq) {
        if (!isLoggedIn()) {
            return 0;
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return likeRepository.selectLike(username, aptSeq);
    }

    private boolean isLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
