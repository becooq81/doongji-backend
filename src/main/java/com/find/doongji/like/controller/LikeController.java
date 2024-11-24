package com.find.doongji.like.controller;

import org.springframework.http.ResponseEntity;

public interface LikeController {

    ResponseEntity<?> toggleLike(String aptSeq);

}
