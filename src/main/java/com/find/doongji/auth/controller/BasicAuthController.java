package com.find.doongji.auth.controller;

import com.find.doongji.auth.payload.request.LoginRequest;
import com.find.doongji.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class BasicAuthController implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
    	boolean success = authService.login(loginRequest, session);
        if (success) {
            return ResponseEntity.ok("User logged in successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @Override
    public ResponseEntity<?> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
