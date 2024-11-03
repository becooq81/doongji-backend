package com.ssafy.home.user.controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.request.UserUpdateRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import com.ssafy.home.user.payload.response.UserSearchResponse;
import com.ssafy.home.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/member")
public class BasicUserController implements UserController {

    @Autowired
    private UserService userService;

    // TODO: make consistent API response
    @Override
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        userService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "User registered successfully"));    }

    @Override
    public ResponseEntity<UserResponse> getUserProfile(HttpSession session) {
        UserResponse userResponse = userService.getUserProfile(session);
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<?> updateUserProfile(@RequestBody UserUpdateRequest userUpdateRequest, HttpSession session) {
        userService.updateUserProfile(userUpdateRequest, session);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "User profile updated successfully"));    
    }

    @Override
    public ResponseEntity<?> deleteUserProfile(HttpSession session) {
        userService.deleteUserProfile(session);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<UserSearchResponse>> searchUsers(@RequestParam String keyword, HttpSession session) {
        List<UserSearchResponse> users = userService.searchUsers(keyword, session);
        return ResponseEntity.ok(users);
    }
    
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
    			.body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    			.body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException ex) {
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    			.body(Collections.singletonMap("message", ex.getMessage()));
    }
}
