package com.ssafy.home.user.service;

import java.util.List;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.request.UserUpdateRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import com.ssafy.home.user.payload.response.UserSearchResponse;

import jakarta.servlet.http.HttpSession;

public interface UserService {
	
    void registerUser(SignUpRequest signUpRequest);
    
    UserResponse getUserProfile(HttpSession session);
    
    void updateUserProfile(UserUpdateRequest userUpdateRequest, HttpSession session);
    
    void deleteUserProfile(HttpSession session);
    
    List<UserSearchResponse> searchUsers(String keyword, HttpSession session);
}
