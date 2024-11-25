package com.find.doongji.like.service;

import com.find.doongji.apt.payload.response.AptInfo;
import com.find.doongji.apt.repository.AptRepository;
import com.find.doongji.auth.service.AuthService;
import com.find.doongji.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicLikeService implements LikeService {

    private final LikeRepository likeRepository;
    private final AuthService authService;
    private final AptRepository aptRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<AptInfo> getAllLikes() {
        if (!authService.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to view likes.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> likedAptSeq = likeRepository.selectAllLikes(username); // aptSeq list
        return likedAptSeq.stream().map(aptRepository::selectAptInfoByAptSeq).collect(Collectors.toList());
    }

    private void validateRequest(String aptSeq) {
        if (aptSeq == null || aptSeq.trim().isEmpty()) {
            throw new IllegalArgumentException("AptSeq must not be null or empty");
        }
    }
}
