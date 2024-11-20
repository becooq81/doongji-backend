package com.find.doongji.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.find.doongji.member.payload.request.SignUpRequest;
import com.find.doongji.member.payload.request.UserUpdateRequest;
import com.find.doongji.member.payload.response.UserResponse;
import com.find.doongji.member.payload.response.UserSearchResponse;

import jakarta.servlet.http.HttpSession;

public interface MemberController {
	
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
