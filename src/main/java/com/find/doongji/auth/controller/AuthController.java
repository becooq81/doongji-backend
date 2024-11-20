package com.find.doongji.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import com.find.doongji.auth.payload.request.LoginRequest;


public interface AuthController {
	
    ResponseEntity<?> login(LoginRequest loginRequest);
	
    ResponseEntity<?> logout(HttpServletRequest request);
}
