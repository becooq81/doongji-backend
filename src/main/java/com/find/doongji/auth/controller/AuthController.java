package com.find.doongji.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.find.doongji.auth.payload.request.LoginRequest;

import jakarta.servlet.http.HttpSession;

public interface AuthController {
	
	@PostMapping("/login")
    ResponseEntity<?> login(LoginRequest loginRequest, HttpSession session);
	
    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpSession session);
}
