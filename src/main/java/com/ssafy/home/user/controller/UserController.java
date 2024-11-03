package com.ssafy.home.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.request.UserUpdateRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import com.ssafy.home.user.payload.response.UserSearchResponse;

import jakarta.servlet.http.HttpSession;

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
