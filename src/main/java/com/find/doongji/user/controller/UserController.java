package com.find.doongji.user.controller;

import com.find.doongji.user.payload.request.SignUpRequest;
import com.find.doongji.user.payload.request.UserUpdateRequest;
import com.find.doongji.user.payload.response.UserResponse;
import com.find.doongji.user.payload.response.UserSearchResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

public interface UserController {
	
    @PostMapping
    ResponseEntity<?> registerUser(SignUpRequest signUpRequest);
    
    @GetMapping("/my")
    ResponseEntity<UserResponse> getUserProfile(HttpSession session);
    
    @PutMapping("/my")
    ResponseEntity<?> updateUserProfile(UserUpdateRequest userUpdateRequest, HttpSession session);
    
    @DeleteMapping("/my")
    ResponseEntity<?> deleteUserProfile(HttpSession session);
    
    @GetMapping("/search")
    ResponseEntity<List<UserSearchResponse>> searchUsers(String keyword, HttpSession session);
}
