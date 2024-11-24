package com.find.doongji.like.service;

import com.find.doongji.apt.payload.response.AptInfo;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

public interface LikeService {

    /**
     * 좋아요 생성 (좋아요 존재 시 삭제)
     * @param aptSeq
     */
    void toggleLike(String aptSeq) throws AccessDeniedException;

    /**
     * 좋아요 여부 조회
     * @param aptSeq
     */
    int viewLike(String aptSeq);

    /**
     * 로그인한 사용자에 대한 모든 좋아요 조회
     */
    List<AptInfo>  getAllLikes();
}
