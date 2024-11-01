package com.ssafy.home.user.service;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.request.UserUpdateRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import com.ssafy.home.user.payload.response.UserSearchResponse;
import com.ssafy.home.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BasicUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(SignUpRequest signUpRequest) {
        // 사용자 이름 중복 확인
        if (userRepository.countByUsername(signUpRequest.getUsername()) > 0) {
            throw new IllegalArgumentException("Username already exists");
        }

        // 이메일 중복 확인
        if (userRepository.countByEmail(signUpRequest.getEmail()) > 0) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 비밀번호와 확인 비밀번호 일치 확인
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // 이름 유효성 검사
        if (signUpRequest.getName() == null || signUpRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        userRepository.insertUser(signUpRequest);
    }

    @Override
    public UserResponse getUserProfile(HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            throw new SecurityException("User not authenticated");
        }

        UserResponse userResponse = userRepository.findByUsername(currentUsername);
        if (userResponse == null) {
            throw new IllegalArgumentException("User not found");
        }

        return userResponse;
    }



    @Override
    public void updateUserProfile(UserUpdateRequest userUpdateRequest, HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            throw new SecurityException("User not authenticated");
        }

        userRepository.updateUser(currentUsername, userUpdateRequest);
    }

    @Override
    public void deleteUserProfile(HttpSession session) {
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            throw new SecurityException("User not authenticated");
        }

        userRepository.deleteUser(currentUsername);
    }

    @Override
    public List<UserSearchResponse> searchUsers(String keyword) {
        // 키워드 유효성 검사
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid query parameter");
        }

        // 사용자 검색
        return userRepository.searchUsers(keyword);
    }
}
