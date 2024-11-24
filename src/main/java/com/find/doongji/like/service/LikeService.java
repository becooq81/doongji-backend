package com.find.doongji.like.service;

import java.nio.file.AccessDeniedException;

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
    int isLiked(String aptSeq);
}
