package com.find.doongji.auth.controller;

import com.find.doongji.auth.payload.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;


public interface AuthController {

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> logout(HttpServletRequest request);
}
