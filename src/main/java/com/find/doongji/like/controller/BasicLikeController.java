package com.find.doongji.like.controller;

import com.find.doongji.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class BasicLikeController implements LikeController {

    private final LikeService likeService;

    @Override
    @PostMapping("/{aptSeq}")
    public ResponseEntity<?> toggleLike(@PathVariable String aptSeq) {
        try {
            likeService.toggleLike(aptSeq);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully toggled like for aptSeq: " + aptSeq);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred while toggling like for aptSeq: " + aptSeq);
        }
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<?> getAllLikes() {
        try {
            return ResponseEntity.ok(likeService.getAllLikes());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("An error occurred while retrieving all likes.");
        }
    }
}
