package com.ssafy.home.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.ssafy.home.auth.payload.request.LoginRequest;

import jakarta.servlet.http.HttpSession;

public interface AuthController {
	
	@PostMapping("/login")
    ResponseEntity<?> login(LoginRequest loginRequest, HttpSession session);
	
    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpSession session);
}
