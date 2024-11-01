package com.ssafy.home.user.controller;

import com.ssafy.home.user.payload.request.SignUpRequest;
import com.ssafy.home.user.payload.request.UserUpdateRequest;
import com.ssafy.home.user.payload.response.UserResponse;
import com.ssafy.home.user.payload.response.UserSearchResponse;
import com.ssafy.home.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<List<UserSearchResponse>> searchUsers(@RequestParam String keyword) {
        List<UserSearchResponse> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }
}
