package com.ssafy.home.user.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.request.UserUpdateRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import com.ssafy.home.user.payload.response.UserSearchResponse;
import com.ssafy.home.user.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class BasicUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(SignUpRequest signUpRequest) {
    	
    	if (userRepository.countByUsername(signUpRequest.getUsername()) > 0) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.countByEmail(signUpRequest.getEmail()) > 0) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (signUpRequest.getName() == null || signUpRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        userRepository.insertUser(signUpRequest);
    }

    @Override
    public UserResponse getUserProfile(HttpSession session) {
    	
        String currentUsername = (String) session.getAttribute("username");
        
        if (currentUsername == null) {
            throw new SecurityException("User not logged in");
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

        if (userUpdateRequest.getEmail() == null || !userUpdateRequest.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (userUpdateRequest.getName() == null || userUpdateRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        userRepository.updateUser(currentUsername, userUpdateRequest);
    }

    @Override
    public void deleteUserProfile(HttpSession session) {
    	
        String currentUsername = (String) session.getAttribute("username");
        
        if (currentUsername == null) {
            throw new SecurityException("User not logged in");
        }

        userRepository.deleteUser(currentUsername);
    }

    @Override
    public List<UserSearchResponse> searchUsers(String keyword, HttpSession session) {

    	String currentUsername = (String) session.getAttribute("username");
    	
        if (currentUsername == null) {
            throw new SecurityException("User not logged in");
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid query parameter");
        }

        List<UserSearchResponse> users = userRepository.searchUsers(keyword);
        if (users.isEmpty()) {
            throw new NoSuchElementException("No users found with the given keyword");
        }

        return users;
    }
}
