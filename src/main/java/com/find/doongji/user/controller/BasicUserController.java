package com.find.doongji.user.controller;

import com.find.doongji.user.payload.request.SignUpRequest;

import com.find.doongji.user.payload.request.UserUpdateRequest;
import com.find.doongji.user.payload.response.UserResponse;
import com.find.doongji.user.payload.response.UserSearchResponse;
import com.find.doongji.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/member")
public class BasicUserController implements UserController {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        userService.registerUser(signUpRequest);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @Override
    public ResponseEntity<UserResponse> getUserProfile(HttpSession session) {
        UserResponse userResponse = userService.getUserProfile(session);
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<?> updateUserProfile(@RequestBody UserUpdateRequest userUpdateRequest, HttpSession session) {
        userService.updateUserProfile(userUpdateRequest, session);
        return ResponseEntity.ok("User profile updated successfully");
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
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
